package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.CommonUtils.Utils.StringUtils
import Plugins.UserServersApi.UseRegisterMessage
import Tables.UserTable
import monix.eval.Task

object UseRegisterMessagePlanner extends APIPlanner[UseRegisterMessage]{
  override protected def plan(api: UseRegisterMessage)(implicit uuid: PlanUUID): Task[String] = {
    //Generate a random string as a token with a self-determined length, which is located in CommonUtils
    val token = StringUtils.randomLetterString(12)
    //加入一个学号属性UserTable.addEntry(api.userName,api.email,api.password,token) >>
    UserTable.addEntry(api.userName,api.email,api.password,token,api.studentId) >>
      Task.now(token)
  }
}