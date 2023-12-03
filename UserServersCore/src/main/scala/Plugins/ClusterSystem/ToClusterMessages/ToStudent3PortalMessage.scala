
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToStudent3PortalMessage.student3PortalRoute

case class ToStudent3PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = student3PortalRoute
}
object ToStudent3PortalMessage{
  val student3PortalRoute: MQRoute =MQRoute("student3PortalRoute")
}
