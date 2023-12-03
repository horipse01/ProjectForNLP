package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.UserServersApi.UserLoginMessage
import Tables.UserTable
import monix.eval.Task

object UserLoginMessagePlanner extends APIPlanner[UserLoginMessage]{
  override protected def plan(api: UserLoginMessage)(implicit uuid: PlanUUID): Task[String] = {
    //现在有错UserTable.checkPassword(api.userName, api.password)
    UserTable.checkPassword(api.studentId, api.password)
  }
}