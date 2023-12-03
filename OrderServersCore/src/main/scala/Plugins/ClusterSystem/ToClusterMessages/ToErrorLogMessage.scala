package Plugins.ClusterSystem.ToClusterMessages

import Plugins.ClusterSystem.ToClusterMessages.ToErrorLogMessage.errorLogRoute

case class ToErrorLogMessage(override val serializedInfo : String) extends ToClusterMessage(serializedInfo) {
  override def route: MQRoute = errorLogRoute
}

object ToErrorLogMessage{
  val errorLogRoute: MQRoute = MQRoute("errorLogRoute")
}

