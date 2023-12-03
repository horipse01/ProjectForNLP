
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToWShop3Message.wShop3Route

case class ToWShop3Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = wShop3Route
}
object ToWShop3Message{
  val wShop3Route: MQRoute =MQRoute("wShop3Route")
}
