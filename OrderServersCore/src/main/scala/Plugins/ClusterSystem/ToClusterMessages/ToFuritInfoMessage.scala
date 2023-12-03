
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToFuritInfoMessage.furitInfoRoute

case class ToFuritInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = furitInfoRoute
}
object ToFuritInfoMessage{
  val furitInfoRoute: MQRoute =MQRoute("furitInfoRoute")
}
