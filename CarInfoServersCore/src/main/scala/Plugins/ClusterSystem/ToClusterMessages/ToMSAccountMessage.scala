package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToMSAccountMessage.msAccountRoute

case class ToMSAccountMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = msAccountRoute
}
object ToMSAccountMessage{
  val msAccountRoute: MQRoute =MQRoute("msAccountRoute")
}