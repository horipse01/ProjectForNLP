
package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToTestService04Message.testService04Route

case class ToTestService04Message(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = testService04Route
}
object ToTestService04Message{
  val testService04Route: MQRoute =MQRoute("testService04Route")
}
