
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToOrderServerMessage.orderServerRoute

case class ToOrderServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = orderServerRoute
}
object ToOrderServerMessage{
  val orderServerRoute: MQRoute =MQRoute("orderServerRoute")
}
