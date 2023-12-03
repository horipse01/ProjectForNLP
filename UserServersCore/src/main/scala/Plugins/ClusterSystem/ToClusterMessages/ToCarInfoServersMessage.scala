
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToCarInfoServersMessage.carInfoServersRoute

case class ToCarInfoServersMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = carInfoServersRoute
}
object ToCarInfoServersMessage{
  val carInfoServersRoute: MQRoute =MQRoute("carInfoServersRoute")
}
