package Plugins.ClusterSystem.ErrorLog

import Plugins.ClusterSystem.Extension.ClusterAPIExtended
import Plugins.ClusterSystem.ToClusterMessages.{MQRoute, ToErrorLogMessage}

/** 这个Message是发给ErrorLog微服务的 */
case class ErrorLogMessage(time:Long, error:Throwable) extends ClusterAPIExtended[Unit] {
  override def getRoute: MQRoute = ToErrorLogMessage.errorLogRoute
}
