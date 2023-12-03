
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToHHUserInfoMessage.hHUserInfoRoute

case class ToHHUserInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = hHUserInfoRoute
}
object ToHHUserInfoMessage{
  val hHUserInfoRoute: MQRoute =MQRoute("hHUserInfoRoute")
}
