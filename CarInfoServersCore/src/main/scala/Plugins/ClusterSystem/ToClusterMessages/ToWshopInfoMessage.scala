
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToWshopInfoMessage.wshopInfoRoute

case class ToWshopInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = wshopInfoRoute
}
object ToWshopInfoMessage{
  val wshopInfoRoute: MQRoute =MQRoute("wshopInfoRoute")
}
