
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToWShopPortalMessage.wShopPortalRoute

case class ToWShopPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = wShopPortalRoute
}
object ToWShopPortalMessage{
  val wShopPortalRoute: MQRoute =MQRoute("wShopPortalRoute")
}
