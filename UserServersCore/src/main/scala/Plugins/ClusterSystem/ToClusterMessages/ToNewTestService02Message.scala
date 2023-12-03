
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToNewTestService02Message.newTestService02Route

case class ToNewTestService02Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = newTestService02Route
}
object ToNewTestService02Message{
  val newTestService02Route: MQRoute =MQRoute("newTestService02Route")
}
