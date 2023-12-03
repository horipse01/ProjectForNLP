
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToTimeOutTestMessage.timeOutTestRoute

case class ToTimeOutTestMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = timeOutTestRoute
}
object ToTimeOutTestMessage{
  val timeOutTestRoute: MQRoute =MQRoute("timeOutTestRoute")
}
