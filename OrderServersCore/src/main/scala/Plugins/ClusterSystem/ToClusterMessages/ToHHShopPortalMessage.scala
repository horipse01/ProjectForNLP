
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHShopPortalMessage.hHShopPortalRoute

case class ToHHShopPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHShopPortalRoute
}
object ToHHShopPortalMessage{
  val hHShopPortalRoute: MQRoute =MQRoute("hHShopPortalRoute")
}
