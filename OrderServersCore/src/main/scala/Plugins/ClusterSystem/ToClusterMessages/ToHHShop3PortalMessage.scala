
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHShop3PortalMessage.hHShop3PortalRoute

case class ToHHShop3PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHShop3PortalRoute
}
object ToHHShop3PortalMessage{
  val hHShop3PortalRoute: MQRoute =MQRoute("hHShop3PortalRoute")
}
