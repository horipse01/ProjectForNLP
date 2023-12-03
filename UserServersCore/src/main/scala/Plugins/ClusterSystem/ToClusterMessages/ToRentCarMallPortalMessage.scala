
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToRentCarMallPortalMessage.rentCarMallPortalRoute

case class ToRentCarMallPortalMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = rentCarMallPortalRoute
}
object ToRentCarMallPortalMessage{
  val rentCarMallPortalRoute: MQRoute =MQRoute("rentCarMallPortalRoute")
}
