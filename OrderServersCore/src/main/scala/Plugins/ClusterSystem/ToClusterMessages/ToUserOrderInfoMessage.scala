
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUserOrderInfoMessage.userOrderInfoRoute

case class ToUserOrderInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = userOrderInfoRoute
}
object ToUserOrderInfoMessage{
  val userOrderInfoRoute: MQRoute =MQRoute("userOrderInfoRoute")
}
