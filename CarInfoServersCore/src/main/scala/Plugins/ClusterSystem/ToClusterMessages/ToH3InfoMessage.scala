
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToH3InfoMessage.h3InfoRoute

case class ToH3InfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = h3InfoRoute
}
object ToH3InfoMessage{
  val h3InfoRoute: MQRoute =MQRoute("h3InfoRoute")
}
