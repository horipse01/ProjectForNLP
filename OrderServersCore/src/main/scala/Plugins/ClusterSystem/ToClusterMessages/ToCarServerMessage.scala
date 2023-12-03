
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToCarServerMessage.carServerRoute

case class ToCarServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = carServerRoute
}
object ToCarServerMessage{
  val carServerRoute: MQRoute =MQRoute("carServerRoute")
}
