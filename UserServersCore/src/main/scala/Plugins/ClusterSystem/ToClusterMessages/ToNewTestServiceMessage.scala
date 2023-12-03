
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToNewTestServiceMessage.newTestServiceRoute

case class ToNewTestServiceMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = newTestServiceRoute
}
object ToNewTestServiceMessage{
  val newTestServiceRoute: MQRoute =MQRoute("newTestServiceRoute")
}
