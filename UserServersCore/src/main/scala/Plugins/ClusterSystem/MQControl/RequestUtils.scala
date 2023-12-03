package Plugins.ClusterSystem.MQControl

import Plugins.ClusterSystem.Extension.APIHandler
import Plugins.ClusterSystem.MQControl.RabbitWorker.{publishMessage, queueName}
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import Plugins.CommonUtils.TypedSystem.API.{API, PlanUUID}
import Plugins.CommonUtils.Utils.IOUtils
import Plugins.CommonUtils.Utils.IOUtils.resultToReply
import com.rabbitmq.client.AMQP.BasicProperties
import monix.eval.Task
import java.util
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import monix.execution.Scheduler.Implicits.global
import scala.util.Try

object RequestUtils {
  def newBroadcast[A <: API](sourceRoute: MQRoute, api: A, deliveryAction: () => Unit, filter1: String, filter2: String): Unit = {
    /** 新的请求发过来，首先给一个回应 */
    Task.defer {
      deliveryAction()
      APIHandler.process(api)
    }.timeout(processTimeOut).runToFuture.onComplete { result: Try[A#ReturnType] => {
      /** 如果API会产生一个reply，我们会把这个reply发回去；否则我们就什么都不干 */
      if (api.hasReply) {
        val replyMessage = resultToReply[api.ReturnType](result.asInstanceOf[Try[api.ReturnType]], api.uuid)(api.getReturnTypeTag, api.getReturnClassTag)
        /** 把这个回复整理一下发出去 */
        val properties = new util.HashMap[String, Object]()
        properties.put("messageType", api.getClass.getSimpleName)
        properties.put("filter1", filter1)
        properties.put("filter2", filter2)

        RabbitWorker.fanoutString(sourceRoute, IOUtils.serialize(replyMessage), api.uuid.id,
          new BasicProperties.Builder().headers(
            properties
          ).deliveryMode(2).build()
        )
      }
    }}
  }

  def newRequest[A <: API](sourceRoute: MQRoute, api: A, deliveryAction: () => Unit): Unit = {
    /** 新的请求发过来，首先给一个回应 */
    Task.defer {
      IOUtils.printInfo("stage1:进入newRequest！")(api.uuid)
      deliveryAction()
      IOUtils.printInfo("stage2:通知消息队列完成！")(api.uuid)
      APIHandler.process(api)
    }.timeout(processTimeOut).runToFuture.onComplete { result: Try[A#ReturnType] => {
      /** 如果API会产生一个reply，我们会把这个reply发回去；否则我们就什么都不干 */
      if (api.hasReply) {
        val replyMessage = resultToReply[api.ReturnType](result.asInstanceOf[Try[api.ReturnType]], api.uuid)(api.getReturnTypeTag, api.getReturnClassTag)
        /** 把这个回复整理一下发出去 */
        val properties = new util.HashMap[String, Object]()
        properties.put("source", queueName)
        properties.put("messageType", REPLY)

        publishMessage(sourceRoute, IOUtils.serialize(replyMessage), new BasicProperties.Builder().headers(
          properties
        ).deliveryMode(2).build(), api.uuid.id)
      }
    }
    }
  }

  def newErrorRequest(sourceRoute: MQRoute, err: Throwable, deliveryAction: () => Unit)(implicit typeTag: TypeTag[String], classTag: ClassTag[String], uuid: PlanUUID): Unit = {
    /** 新的请求发过来，首先给一个回应 */
    Task.defer {
      deliveryAction()
      Task.raiseError(err)
    }.timeout(processTimeOut).runToFuture.onComplete { result: Try[String] => {
      /** 如果API会产生一个reply，我们会把这个reply发回去；否则我们就什么都不干 */
      val replyMessage = resultToReply[String](result, uuid)
      /** 把这个回复整理一下发出去 */
      val properties = new util.HashMap[String, Object]()
      properties.put("source", queueName)
      properties.put("messageType", REPLY)

      publishMessage(
        sourceRoute,
        IOUtils.serialize(replyMessage),
        new BasicProperties.Builder().headers(properties).deliveryMode(2).build(), uuid.id
      )
    }}
  }

}
