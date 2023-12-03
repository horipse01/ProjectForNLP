package Plugins.ClusterSystem.MQControl

import Globals.GlobalVariables
import Plugins.ClusterSystem.MQControl.RabbitWorker._
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import Plugins.CommonUtils.TypedSystem.API.PlanUUID.getUUID
import Plugins.CommonUtils.TypedSystem.API.{API, PlanUUID}
import Plugins.CommonUtils.TypedSystem.sendError
import Plugins.CommonUtils.Types.ReplyMessage
import Plugins.CommonUtils.Utils.IOUtils
import Plugins.CommonUtils.Utils.IOUtils.exceptionToString
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.rabbitmq.client._
import com.typesafe.scalalogging.Logger

import java.io.IOException
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.TypeTag
import scala.util.Try

case class BackendConsumer[A <: API : TypeTag](queueName: String, channel: Channel) extends DefaultConsumer(channel) {
  override def handleShutdownSignal(consumerTag: String, sig: ShutdownSignalException): Unit = {
    sig.printStackTrace()
    if (!connection.isOpen) connection = factory.newConnection()
    startBackendConsumer()
  }

  override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
    var deliveryTag: Long = 0L
    var (message, messageType) = ("", "")
    var source: MQRoute = null
    implicit var uuid: PlanUUID = PlanUUID("")

    Try {
      val headers = properties.getHeaders.asInstanceOf[java.util.HashMap[String, Object]]
      def getHeadersProp(propertyName: String): String = headers.get(propertyName).toString

      deliveryTag = envelope.getDeliveryTag
      message = new String(body)
      messageType = getHeadersProp("messageType")
      source = MQRoute(getHeadersProp("source"))

      messageType match {
        case REQUEST =>
          val requestMessage = IOUtils.deserialize[GlobalVariables.APIType](message)
          uuid = requestMessage.uuid
          Logger(rabbitMQConsumerName + "-" + uuid.id).info("收到消息：" + message)
          RequestUtils.newRequest(source, requestMessage, () => channel.basicAck(deliveryTag, false))

        case BROADCAST =>
          val requestMessage = IOUtils.deserialize[GlobalVariables.APIType](message)
          uuid = requestMessage.uuid
          Logger(rabbitMQConsumerName + "-" + uuid.id).info("收到广播消息：" + message)
          RequestUtils.newBroadcast(source, requestMessage, () => channel.basicAck(deliveryTag, false), getHeadersProp("filter1"), getHeadersProp("filter2"))

        case REPLY =>
          val replyMessage = IOUtils.deserialize[ReplyMessage](message)
          uuid = PlanUUID(replyMessage.uuid)
          Logger(rabbitMQConsumerName + "-" + uuid.id).info("收到回复：" + message.take(1000))
          replyBoxRef ! NewReply(replyMessage, () => channel.basicAck(deliveryTag, false))
      }

    }.failed.map(e => {
      if (uuid.id == ""){ uuid = getUUID(message) }

      messageType match {
        case REQUEST | BROADCAST => handleReqException(source, e, deliveryTag, channel)
        case REPLY => handleReplyException(e, deliveryTag, channel)
        case _ => sendError(e, PlanUUID("Delivery-Error"))
      }
    })
  }

  private def handleReqException(source: MQRoute, e: Throwable, deliveryTag: Long, channel: Channel)(implicit typeTag: TypeTag[String], classTag: ClassTag[String], uuid: PlanUUID): Unit = {
    RequestUtils.newErrorRequest(source, e, () => channel.basicAck(deliveryTag, false))
  }

  private def handleReplyException(e: Throwable, deliveryTag: Long, channel: Channel)(implicit typeTag: TypeTag[String], classTag: ClassTag[String], uuid: PlanUUID): Unit = {
    if (e.isInstanceOf[IOException] || e.isInstanceOf[JsonParseException] || e.isInstanceOf[JsonMappingException]) {
      replyBoxRef ! NewReply(ReplyMessage(-2, e.getMessage), () => channel.basicAck(deliveryTag, false))
    } else {
      replyBoxRef ! NewReply(ReplyMessage(-2, GlobalVariables.serviceCode + "0000" + exceptionToString(e)), () => channel.basicAck(deliveryTag, false))
    }
  }

}
