
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToOrderServersMessage.orderServersRoute

case class ToOrderServersMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = orderServersRoute
}
object ToOrderServersMessage{
  val orderServersRoute: MQRoute =MQRoute("orderServersRoute")
}
