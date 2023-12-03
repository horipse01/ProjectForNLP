package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.CommonUtils.Utils.StringUtils
import Plugins.UserServersApi.{GetHistoryMessage, UseRegisterMessage}
import Tables.HistoryTable
import monix.eval.Task

object GetHistoryMessagePlanner extends APIPlanner[GetHistoryMessage]{

  override protected def plan(api: GetHistoryMessage)(implicit uuid: PlanUUID): Task[String] = {
    //用学号去查找历史记录，返回序列化的值
    val list = HistoryTable.getHistory(api.studentId)
    //序列化list为String
    val str = for{
      i <- list
    } yield i.toString
    str
  }
}