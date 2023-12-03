package Plugins.ClusterSystem.MQControl

import Globals.GlobalVariables
import Plugins.ClusterSystem.MQControl.RabbitWorker._
import Plugins.CommonUtils.Utils.IOUtils
import akka.actor.typed.ActorRef
import com.rabbitmq.client._

import scala.reflect.ClassTag

case class BackendWatcher(qName : String, eName : String, watcher : ActorRef[GlobalVariables.APIType], channel : Channel)(implicit tag:ClassTag[GlobalVariables.APIType]) extends DefaultConsumer(channel) {
  override def handleShutdownSignal(consumerTag: String, sig: ShutdownSignalException): Unit = {
    sig.printStackTrace()
    if (!connection.isOpen) connection = factory.newConnection()
    startBackendWatcher(qName, eName, watcher)
  }
  override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties, body: Array[Byte]): Unit = {
    try {
      val deliveryTag = envelope.getDeliveryTag
      val api= IOUtils.deserialize[GlobalVariables.APIType](new String(body))
      watcher ! api
      channel.basicAck(deliveryTag, true)
    } catch {
      case e : Exception =>
        e.printStackTrace()
    }
  }
}
