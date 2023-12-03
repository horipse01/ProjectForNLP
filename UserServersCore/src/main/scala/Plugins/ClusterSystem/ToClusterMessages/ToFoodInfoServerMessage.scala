
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFoodInfoServerMessage.foodInfoServerRoute

case class ToFoodInfoServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = foodInfoServerRoute
}
object ToFoodInfoServerMessage{
  val foodInfoServerRoute: MQRoute =MQRoute("foodInfoServerRoute")
}
