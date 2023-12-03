
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToRentCarPortalMessage.rentCarPortalRoute

case class ToRentCarPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = rentCarPortalRoute
}
object ToRentCarPortalMessage{
  val rentCarPortalRoute: MQRoute =MQRoute("rentCarPortalRoute")
}
