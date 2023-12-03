
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFruitServerMessage.fruitServerRoute

case class ToFruitServerMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = fruitServerRoute
}
object ToFruitServerMessage{
  val fruitServerRoute: MQRoute =MQRoute("fruitServerRoute")
}
