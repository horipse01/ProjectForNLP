
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHLoginMessage.hHLoginRoute

case class ToHHLoginMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHLoginRoute
}
object ToHHLoginMessage{
  val hHLoginRoute: MQRoute =MQRoute("hHLoginRoute")
}
