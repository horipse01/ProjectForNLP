
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToMSShopInfoMessage.mSShopInfoRoute

case class ToMSShopInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = mSShopInfoRoute
}
object ToMSShopInfoMessage{
  val mSShopInfoRoute: MQRoute =MQRoute("mSShopInfoRoute")
}
