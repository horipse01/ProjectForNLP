
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUserServersMessage.userServersRoute

case class ToUserServersMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = userServersRoute
}
object ToUserServersMessage{
  val userServersRoute: MQRoute =MQRoute("userServersRoute")
}
