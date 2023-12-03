
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToStudent2PortalMessage.student2PortalRoute

case class ToStudent2PortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = student2PortalRoute
}
object ToStudent2PortalMessage{
  val student2PortalRoute: MQRoute =MQRoute("student2PortalRoute")
}
