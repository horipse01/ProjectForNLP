
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToTestServiceMessage.testServiceRoute

case class ToTestServiceMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = testServiceRoute
}
object ToTestServiceMessage{
  val testServiceRoute: MQRoute =MQRoute("testServiceRoute")
}
