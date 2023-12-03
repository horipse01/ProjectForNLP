
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToBooksInfoMessage.booksInfoRoute

case class ToBooksInfoMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = booksInfoRoute
}
object ToBooksInfoMessage{
  val booksInfoRoute: MQRoute =MQRoute("booksInfoRoute")
}
