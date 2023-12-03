
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHShop2PortalMessage.hHShop2PortalRoute

case class ToHHShop2PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHShop2PortalRoute
}
object ToHHShop2PortalMessage{
  val hHShop2PortalRoute: MQRoute =MQRoute("hHShop2PortalRoute")
}
