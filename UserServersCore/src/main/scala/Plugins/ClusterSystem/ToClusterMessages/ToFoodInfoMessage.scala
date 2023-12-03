
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFoodInfoMessage.foodInfoRoute

case class ToFoodInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = foodInfoRoute
}
object ToFoodInfoMessage{
  val foodInfoRoute: MQRoute =MQRoute("foodInfoRoute")
}
