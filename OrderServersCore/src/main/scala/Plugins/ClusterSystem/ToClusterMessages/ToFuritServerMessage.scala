
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFuritServerMessage.furitServerRoute

case class ToFuritServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = furitServerRoute
}
object ToFuritServerMessage{
  val furitServerRoute: MQRoute =MQRoute("furitServerRoute")
}
