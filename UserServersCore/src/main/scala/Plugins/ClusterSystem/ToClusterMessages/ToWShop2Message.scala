
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToWShop2Message.wShop2Route

case class ToWShop2Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = wShop2Route
}
object ToWShop2Message{
  val wShop2Route: MQRoute =MQRoute("wShop2Route")
}
