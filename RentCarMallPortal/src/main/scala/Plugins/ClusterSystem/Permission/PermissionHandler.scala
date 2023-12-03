package Plugins.ClusterSystem.Permission

import Plugins.ClusterSystem.Permission.PermissionMessages.PermissionDBInitMessage
import Plugins.CommonUtils.Utils.SchedulerUtils.defaultComputation

object PermissionHandler {
  def init(): Unit = {
    val api=PermissionDBInitMessage()
    api.getPlan(api).runToFuture
  }
}
