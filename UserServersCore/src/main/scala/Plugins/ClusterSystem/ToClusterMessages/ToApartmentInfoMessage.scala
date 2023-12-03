
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToApartmentInfoMessage.apartmentInfoRoute

case class ToApartmentInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = apartmentInfoRoute
}
object ToApartmentInfoMessage{
  val apartmentInfoRoute: MQRoute =MQRoute("apartmentInfoRoute")
}
