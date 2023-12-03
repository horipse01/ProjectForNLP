
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUserInfoServerMessage.userInfoServerRoute

case class ToUserInfoServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = userInfoServerRoute
}
object ToUserInfoServerMessage{
  val userInfoServerRoute: MQRoute =MQRoute("userInfoServerRoute")
}
