
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToRentCarShopPortalMessage.rentCarShopPortalRoute

case class ToRentCarShopPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = rentCarShopPortalRoute
}
object ToRentCarShopPortalMessage{
  val rentCarShopPortalRoute: MQRoute =MQRoute("rentCarShopPortalRoute")
}
