
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToStudent1PortalMessage.student1PortalRoute

case class ToStudent1PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = student1PortalRoute
}
object ToStudent1PortalMessage{
  val student1PortalRoute: MQRoute =MQRoute("student1PortalRoute")
}
