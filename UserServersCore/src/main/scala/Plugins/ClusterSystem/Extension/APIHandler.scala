package Plugins.ClusterSystem.Extension

import Plugins.CommonUtils.TypedSystem.API.API
import Plugins.CommonUtils.Utils.IOUtils
import monix.eval.Task

/** 基本的处理API的逻辑，来了API之后，找到相对应的PlannerSwitch，然后切换一下 */
object APIHandler {
  def process[A<:API](api:A): Task[A#ReturnType] =Task.defer{
    IOUtils.printInfo("stage3:进入APIHandler！")(api.uuid)
    val planner = PlannerSwitch.switch(api)
    IOUtils.printInfo("stage4:完成message到Planner转换！")(api.uuid)
    planner.getPlan(api)
  }
}
