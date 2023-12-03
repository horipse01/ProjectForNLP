package Plugins.ClusterSystem.Permission.PermissionMessages

import Plugins.ClusterSystem.Permission.PermissionTable.checkAllowList
import Plugins.ClusterSystem.Permission.{MessageTypeNotExistException, PermissionMessage, PermissionTable}
import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import monix.eval.Task

case class UpdateAllowListMessage(messageType : Int, newAllowList : List[String]) extends PermissionMessage[String] with APIPlanner[UpdateAllowListMessage]{
  /** api的执行内容 */
  override protected def plan(api: UpdateAllowListMessage)(implicit uuid:PlanUUID): Task[String] = {
    checkAllowList(messageType).flatMap{
      case None=>throw MessageTypeNotExistException()
      case _=> PermissionTable.updateAllowList(messageType, newAllowList)
    }.flatMap(_=>Task("更新成功！"))
  }
}
