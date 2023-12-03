
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToTimeoutTest04Message.timeoutTest04Route

case class ToTimeoutTest04Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = timeoutTest04Route
}
object ToTimeoutTest04Message{
  val timeoutTest04Route: MQRoute =MQRoute("timeoutTest04Route")
}
