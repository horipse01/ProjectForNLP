
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToMSFoodInfoMessage.mSFoodInfoRoute

case class ToMSFoodInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = mSFoodInfoRoute
}
object ToMSFoodInfoMessage{
  val mSFoodInfoRoute: MQRoute =MQRoute("mSFoodInfoRoute")
}
