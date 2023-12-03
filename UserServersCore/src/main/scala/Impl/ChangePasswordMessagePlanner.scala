package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.UserServersApi.ChangePasswordMessage
import Tables.UserTable
import monix.eval.Task

object ChangePasswordMessagePlanner extends APIPlanner[ChangePasswordMessage]{
  override protected def plan(api: ChangePasswordMessage)(implicit uuid: PlanUUID): Task[String] = {
    //先检查旧密码对不对，对的话就改密码并返回1，不对的话，返回-1
    UserTable.checkPassword(api.studentId, api.oldPassword).flatMap(
      //如果旧密码对的话，就改密码
      _ => UserTable.changePassword(api.studentId, api.newPassword).map(
        //改完密码后返回1
        _ => "1"
      )
    ).onErrorHandleWith(
      //如果旧密码不对的话，就返回-1
      _ => Task.now("-1")
    )
  }
}