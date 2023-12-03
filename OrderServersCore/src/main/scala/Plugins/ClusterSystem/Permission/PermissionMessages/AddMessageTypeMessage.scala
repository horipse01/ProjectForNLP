package Plugins.ClusterSystem.Permission.PermissionMessages

import Plugins.ClusterSystem.Permission.PermissionTable.checkAllowList
import Plugins.ClusterSystem.Permission.{MessageTypeAlreadyExistsException, PermissionMessage, PermissionRow, PermissionTable}
import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import monix.eval.Task

case class AddMessageTypeMessage(newMessageType : Int, allowList : List[String]) extends PermissionMessage[String] with APIPlanner[AddMessageTypeMessage]{
  /** api的执行内容 */
  override protected def plan(api: AddMessageTypeMessage)(implicit uuid:PlanUUID): Task[String] =
    checkAllowList(newMessageType).flatMap{
      case None=> PermissionTable.addRow(PermissionRow(newMessageType, allowList))
      case _=> throw MessageTypeAlreadyExistsException()
    }.flatMap(_=>Task("添加成功！"))
}
