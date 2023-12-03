package Plugins.ClusterSystem

import Plugins.CommonUtils.TypedSystem.RootBehavior.ThreadName
import akka.actor.typed.ActorRef

import scala.concurrent.duration.{DurationInt, FiniteDuration, SECONDS}

package object MQControl {
  val REQUEST : String = "Request"
  val REPLY : String = "Reply"
  val BROADCAST: String = "Broadcast"

  val qianFangExchange:String="qianfang"
  val directExchangeType:String="direct"
  val xMessageTTL:String="x-message-ttl"
  val ttlTime:Integer=60000
  val fanout:String="fanout"
  val rabbitMQPublisherName:String="RabbitMQPublisher"
  val rabbitMQPublisherFanoutName:String="RabbitMQPublisher(Fanout)"

  val rabbitMQConsumerName:String="RabbitMQConsumer"

  val processTimeOut: FiniteDuration = FiniteDuration.apply(60L, SECONDS)
  val rabbitWorkerThreadName:ThreadName=ThreadName("RabbitWorker")

  var requestBox: ActorRef[RequestBoxCommand] = _
}

