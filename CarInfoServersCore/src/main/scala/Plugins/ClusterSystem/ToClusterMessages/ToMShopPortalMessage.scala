
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToMShopPortalMessage.mShopPortalRoute

case class ToMShopPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = mShopPortalRoute
}
object ToMShopPortalMessage{
  val mShopPortalRoute: MQRoute =MQRoute("mShopPortalRoute")
}
