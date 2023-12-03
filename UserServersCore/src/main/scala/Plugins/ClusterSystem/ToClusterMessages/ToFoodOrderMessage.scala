
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFoodOrderMessage.foodOrderRoute

case class ToFoodOrderMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = foodOrderRoute
}
object ToFoodOrderMessage{
  val foodOrderRoute: MQRoute =MQRoute("foodOrderRoute")
}
