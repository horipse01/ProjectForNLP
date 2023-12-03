
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToStuLoginMessage.stuLoginRoute

case class ToStuLoginMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = stuLoginRoute
}
object ToStuLoginMessage{
  val stuLoginRoute: MQRoute =MQRoute("stuLoginRoute")
}
