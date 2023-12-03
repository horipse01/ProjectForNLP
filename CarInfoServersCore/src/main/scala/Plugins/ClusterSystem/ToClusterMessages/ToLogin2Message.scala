
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToLogin2Message.login2Route

case class ToLogin2Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = login2Route
}
object ToLogin2Message{
  val login2Route: MQRoute =MQRoute("login2Route")
}
