
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToNewTestService03Message.newTestService03Route

case class ToNewTestService03Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = newTestService03Route
}
object ToNewTestService03Message{
  val newTestService03Route: MQRoute =MQRoute("newTestService03Route")
}
