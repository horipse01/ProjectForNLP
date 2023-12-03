
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUserServerMessage.userServerRoute

case class ToUserServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = userServerRoute
}
object ToUserServerMessage{
  val userServerRoute: MQRoute =MQRoute("userServerRoute")
}
