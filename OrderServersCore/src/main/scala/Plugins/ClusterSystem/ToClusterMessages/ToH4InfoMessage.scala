
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToH4InfoMessage.h4InfoRoute

case class ToH4InfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = h4InfoRoute
}
object ToH4InfoMessage{
  val h4InfoRoute: MQRoute =MQRoute("h4InfoRoute")
}
