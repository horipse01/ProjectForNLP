package Plugins.ClusterSystem.MQControl

import Plugins.ClusterSystem.Extension.ClusterAPI
import Plugins.ClusterSystem.MQControl.RabbitWorker.{publishMessage, queueName, replyBoxRef}
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.sendError
import Plugins.CommonUtils.Types.ReplyMessage
import Plugins.CommonUtils.Utils.IOUtils
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import com.rabbitmq.client.AMQP.BasicProperties
import org.joda.time.DateTime

import java.util
import scala.util.{Failure, Success, Try}

private[MQControl] sealed trait RequestBoxCommand

/** portal转发一个请求 */
case class PortalForwardRequest(route: MQRoute, serializedMessage: String, uuid: PlanUUID, ref: ActorRef[ReplyMessage]) extends RequestBoxCommand

/** 向其他微服务发送一个请求 */
case class SendRequestToBox(api: ClusterAPI, ref: ActorRef[ReplyMessage]) extends RequestBoxCommand

object RequestBox {

  val namePrefix = "requestBox"
  implicit val uuid: PlanUUID = PlanUUID(namePrefix)

  def workBehavior(): Behavior[RequestBoxCommand] = {

    Behaviors.receiveMessage[RequestBoxCommand] { command =>
      Try {
        command match {
          case PortalForwardRequest(route, serializedMessage, uuid, ref) =>
            /** 发送给ReplyBox */
            replyBoxRef ! NewRequest(DateTime.now().getMillis, uuid, ref)
            /** 把这个请求整理一下发送出去 */
            val properties = new util.HashMap[String, Object]()
            properties.put("source", queueName)
            properties.put("messageType", REQUEST)

            publishMessage(route, serializedMessage,
              new BasicProperties.Builder().headers(properties).deliveryMode(2).build(), uuid.id)

            Behaviors.same[RequestBoxCommand]
          case SendRequestToBox(api, ref) =>
            /** 发送给ReplyBox */
            replyBoxRef ! NewRequest(DateTime.now().getMillis, api.uuid, ref)
            /** 把这个请求整理一下发送出去 */
            val target = api.getRoute
            val properties = new util.HashMap[String, Object]()
            properties.put("source", queueName)
            properties.put("messageType", REQUEST)

            publishMessage(target, IOUtils.serialize(api), new BasicProperties.Builder().headers(
              properties
            ).deliveryMode(2).build(), api.uuid.id)

            Behaviors.same[RequestBoxCommand]
        }
      } match {
        case Success(v) => v
        case Failure(exception) =>
          sendError(exception, uuid)
          Behaviors.same
      }
    }.receiveSignal {
      case (_, akka.actor.typed.PreRestart) =>
        IOUtils.printWarning("RequestBox重启！")
        Behaviors.same
    }
  }

  def apply(parent: ActorRef[RabbitWorkerCommand]): Behavior[RequestBoxCommand] =
    Behaviors.supervise {
      Behaviors.setup[RequestBoxCommand] { ctx =>
        /** 设置一个adapter，用于接受需要发送的API请求 */
        requestBox = ctx.self
        parent ! FinishedBox(ctx.self.path.name)
        workBehavior()
      }
    }.onFailure(SupervisorStrategy.restart.withStashCapacity(1000))
}
