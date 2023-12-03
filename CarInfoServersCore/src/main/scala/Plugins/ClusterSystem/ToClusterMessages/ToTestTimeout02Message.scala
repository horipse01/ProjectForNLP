
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToTestTimeout02Message.testTimeout02Route

case class ToTestTimeout02Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = testTimeout02Route
}
object ToTestTimeout02Message{
  val testTimeout02Route: MQRoute =MQRoute("testTimeout02Route")
}
