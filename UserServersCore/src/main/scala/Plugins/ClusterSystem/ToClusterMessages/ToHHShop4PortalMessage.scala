
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHShop4PortalMessage.hHShop4PortalRoute

case class ToHHShop4PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHShop4PortalRoute
}
object ToHHShop4PortalMessage{
  val hHShop4PortalRoute: MQRoute =MQRoute("hHShop4PortalRoute")
}
