
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToUsersInfoMessage.usersInfoRoute

case class ToUsersInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = usersInfoRoute
}
object ToUsersInfoMessage{
  val usersInfoRoute: MQRoute =MQRoute("usersInfoRoute")
}
