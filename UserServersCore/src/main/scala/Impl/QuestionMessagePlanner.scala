package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.CommonUtils.Utils.StringUtils
import Plugins.UserServersApi.QuestionMessage
import Tables.UserTable
import monix.eval.Task

object QuestionMessagePlanner extends APIPlanner[QuestionMessage]{
  override protected def plan(api: QuestionMessage)(implicit uuid: PlanUUID): Task[String] = {
    val res = UserTable.checkToken(api.userToken)
    for {
      i <- res
      r <- if (i) Task.now("success") else Task.now("fail")
    } yield r
  }
}