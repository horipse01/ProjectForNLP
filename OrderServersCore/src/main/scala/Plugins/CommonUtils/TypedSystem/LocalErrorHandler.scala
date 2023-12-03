package Plugins.CommonUtils.TypedSystem

import Plugins.CommonUtils.TypedSystem.API.PlanUUID

object LocalErrorHandler {
  def send(exception: Throwable, uuid:PlanUUID): Unit = exception.printStackTrace()
}
