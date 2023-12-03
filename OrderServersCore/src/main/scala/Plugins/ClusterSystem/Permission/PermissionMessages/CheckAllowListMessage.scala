package Plugins.ClusterSystem.Permission.PermissionMessages

import Plugins.ClusterSystem.Permission.{MessageTypeNotExistException, PermissionMessage, PermissionTable}
import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import monix.eval.Task

case class CheckAllowListMessage(messageType : Int) extends PermissionMessage[List[String]] with APIPlanner[CheckAllowListMessage]{
  /** api的执行内容 */
  override protected def plan(api: CheckAllowListMessage)(implicit uuid:PlanUUID): Task[List[String]] =
    PermissionTable.checkAllowList(messageType).flatMap{
      case None=>throw MessageTypeNotExistException()
      case Some(value)=> Task(value)
    }
}
