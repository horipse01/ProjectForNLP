package Plugins.ClusterSystem.Permission.PermissionMessages

import Plugins.ClusterSystem.Permission.{PermissionMessage, PermissionRow, PermissionTable}
import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import monix.eval.Task

case class CheckAllMessageTypeMessage() extends PermissionMessage[Seq[PermissionRow]] with APIPlanner[CheckAllMessageTypeMessage]{
  /** api的执行内容 */
  override protected def plan(api: CheckAllMessageTypeMessage)(implicit uuid:PlanUUID): Task[Seq[PermissionRow]] =
    PermissionTable.checkAllMessageType()
}
