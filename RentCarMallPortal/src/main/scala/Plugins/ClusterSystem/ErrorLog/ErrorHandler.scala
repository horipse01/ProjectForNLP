package Plugins.ClusterSystem.ErrorLog

import Plugins.ClusterSystem.Extension.ClusterEventGenerator.sendAction
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.akkaSystem
import akka.actor.typed.ActorSystem
import akka.util.Timeout
import Plugins.CommonUtils.Utils.SchedulerUtils.defaultComputation
import org.joda.time.DateTime

import java.util.concurrent.TimeUnit

object ErrorHandler {
  def send(exception:Throwable, uuid:PlanUUID) :Unit= {
    implicit val qianFangSystem: ActorSystem[Nothing] = akkaSystem
    implicit val timeout: Timeout = Timeout(90, TimeUnit.SECONDS)
    exception.printStackTrace()
    sendAction(ErrorLogMessage(DateTime.now.getMillis, exception))(uuid).runToFuture
  }
}
