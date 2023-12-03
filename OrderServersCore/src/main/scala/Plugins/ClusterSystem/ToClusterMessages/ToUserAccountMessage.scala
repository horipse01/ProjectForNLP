
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUserAccountMessage.userAccountRoute

case class ToUserAccountMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = userAccountRoute
}
object ToUserAccountMessage{
  val userAccountRoute: MQRoute =MQRoute("userAccountRoute")
}
