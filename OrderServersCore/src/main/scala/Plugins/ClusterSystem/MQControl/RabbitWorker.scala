package Plugins.ClusterSystem.MQControl

import Globals.GlobalVariables
import Globals.GlobalVariables.serviceCode
import Plugins.ClusterSystem.Exceptions.ServiceCodeNotFoundInRouteMapException
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import Plugins.ClusterSystem.ToClusterMessages.RouteMap.routeMap
import Plugins.CommonUtils.TypedSystem.RootBehavior.{AkkaThreadInfo, RootCommand, ThreadFinish, ThreadName}
import Plugins.CommonUtils.UserPath
import Plugins.CommonUtils.Utils.IOUtils
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.actor.typed.{ActorRef, Behavior}
import com.rabbitmq.client.AMQP.BasicProperties
import com.rabbitmq.client._
import com.typesafe.scalalogging.Logger

import java.util
import scala.util.Try

sealed trait RabbitWorkerCommand

case class FinishedBox(name: String) extends RabbitWorkerCommand

case class ReadyToStart() extends RabbitWorkerCommand

case class RabbitWorkerState(boxSet: Set[String])

object RabbitWorker{
  private val threadName: ThreadName = rabbitWorkerThreadName

  def threadInfo:AkkaThreadInfo= AkkaThreadInfo(setter, threadName, List())

  def setter(root : ActorContext[RootCommand]):Unit= {
    root.spawn(apply(root.self),threadName.name)
  }

  lazy val factory: ConnectionFactory = genFactory()
  var connection : Connection = _
  var publishChannel : Channel = _
  var queueName : String = _
  private[MQControl] var replyBoxRef:ActorRef[ReplyBoxCommand]=_
  private[MQControl] var requestBoxRef:ActorRef[RequestBoxCommand]=_

  def genFactory() : ConnectionFactory = {
    val newFactory = new ConnectionFactory()
    newFactory.setUsername(UserPath.chosenPath.mqUsername)
    newFactory.setPassword(UserPath.chosenPath.mqPassword)
    newFactory.setHost(UserPath.chosenPath.mqHost)
    newFactory.setPort(UserPath.chosenPath.mqPort)
    newFactory.setAutomaticRecoveryEnabled(true)
    newFactory.setNetworkRecoveryInterval(5000)
    newFactory
  }
  def doubleCheck(directOpen:Boolean=false): Unit ={
    checkConnection(directOpen)
    checkChannel(directOpen)
  }
  def checkConnection(directOpen:Boolean=false):Unit= if (directOpen || !connection.isOpen) connection = factory.newConnection()
  def checkChannel(directOpen:Boolean=false):Unit= if (directOpen || !publishChannel.isOpen) {
    publishChannel = connection.createChannel()
    publishChannel.confirmSelect()
  }

  def publishChannelDeclare():util.HashMap[String,Object]={
    publishChannel.exchangeDeclare(qianFangExchange, directExchangeType, true)
    val args = new util.HashMap[String, Object]()
    args.put(xMessageTTL, ttlTime)
    args
  }

  /** 匿名初始化，返回队列名 */
  def anonymousInit() : String = {
    doubleCheck(true)
    publishChannelDeclare()
    Try(queueName = publishChannel.queueDeclare().getQueue).failed.map{_=>
      checkChannel(true)
      queueName = publishChannel.queueDeclare().getQueue
    }
    publishChannel.queueBind(queueName, qianFangExchange, queueName)
    queueName
  }
  def initCustomRoute(mqRoute: MQRoute) : Unit = {
    doubleCheck(true)
    queueName = mqRoute.name
    val args=publishChannelDeclare()
    Try{
      publishChannel.queueDelete(mqRoute.name)
      publishChannel.queueDeclare(mqRoute.name, true, false, false, args)
    }.failed.map{_=>
      checkChannel(true)
      publishChannel.queueDelete(mqRoute.name)
      publishChannel.queueDeclare(mqRoute.name, true, false, false, args)
    }
    publishChannel.queueBind(mqRoute.name, qianFangExchange, mqRoute.name)
  }
  def init() : Unit = {
    val mqRoute=routeMap.getOrElse(serviceCode, throw ServiceCodeNotFoundInRouteMapException(serviceCode))
    initCustomRoute(mqRoute)
  }


  def startBackendConsumer() : Unit = {
    checkConnection()
    val channel = connection.createChannel()
    channel.basicConsume(queueName, false, BackendConsumer(queueName, channel))
  }

  def startBackendWatcher(qName : String, eName : String, watcher : ActorRef[GlobalVariables.APIType]) : Unit = {
    checkConnection()
    var channel = connection.createChannel()
    channel.exchangeDeclare(eName, fanout, true)
    val args = new util.HashMap[String, Object]()
    args.put(xMessageTTL, ttlTime)
    Try(channel.queueDeclare(qName, false, false, true, args)).failed.map{_=>
      channel = connection.createChannel()
      channel.queueDelete(qName)
      channel.queueDeclare(qName, false, false, true, args)
    }
    channel.queueBind(qName, eName, qName)
    channel.basicConsume(qName, false, BackendWatcher(qName, eName, watcher, channel))
  }

  def publishMessage(route : MQRoute, serializedInfo: String, headers :AMQP.BasicProperties, uuid: String): Unit = {
    doubleCheck()
    publishChannel.basicPublish(qianFangExchange, route.name, headers, serializedInfo.getBytes)
    if (publishChannel.waitForConfirms())
      Logger(rabbitMQPublisherName + "-" + uuid).info(serializedInfo.take(1000) + " 发送成功！")
    else
      Logger(rabbitMQPublisherName + "-" + uuid).error(serializedInfo.take(1000) + " 发送失败！")
  }

  def declareExchange(exchangeRoute : MQRoute, exchangeType : String, durable : Boolean = true) : Unit = {
    doubleCheck()
    publishChannel.exchangeDeclare(exchangeRoute.name, exchangeType, durable)
  }

  def fanoutString(exchangeRoute: MQRoute, message: String, uuid: String,
                   headers: AMQP.BasicProperties= new BasicProperties.Builder().deliveryMode(2).build()): Unit = {
    doubleCheck()
    publishChannel.basicPublish(exchangeRoute.name, "", headers, message.getBytes)
    if (publishChannel.waitForConfirms())
      Logger(rabbitMQPublisherFanoutName + "-" + uuid).info(message+ " 发送成功！")
    else
      Logger(rabbitMQPublisherFanoutName + "-" + uuid).error(message + " 发送失败！")
  }

  def apply(root:ActorRef[RootCommand]) : Behavior[RabbitWorkerCommand] = Behaviors.setup[RabbitWorkerCommand] { ctx =>
    replyBoxRef = ctx.spawn(ReplyBox(ctx.self), ReplyBox.namePrefix)
    requestBoxRef = ctx.spawn(RequestBox(ctx.self), RequestBox.namePrefix)
    val state = RabbitWorkerState(Set())
    rabbitWorkerBehaviors(root, ctx, state)
  }

  def rabbitWorkerBehaviors(root:ActorRef[RootCommand], ctx: ActorContext[RabbitWorkerCommand], state: RabbitWorkerState): Behavior[RabbitWorkerCommand] = {
    Behaviors.receiveMessage[RabbitWorkerCommand] {
      case FinishedBox(name) =>
        val curBoxSet = state.boxSet + name
        if (curBoxSet.exists(_.startsWith(ReplyBox.namePrefix)) && curBoxSet.exists(_.startsWith(RequestBox.namePrefix))) {
          ctx.self ! ReadyToStart()
        }
        rabbitWorkerBehaviors(root, ctx, state.copy(boxSet = curBoxSet))
      case ReadyToStart() =>
        root ! ThreadFinish(threadName)
        startBackendConsumer()
        Behaviors.same
    }
  }

}

