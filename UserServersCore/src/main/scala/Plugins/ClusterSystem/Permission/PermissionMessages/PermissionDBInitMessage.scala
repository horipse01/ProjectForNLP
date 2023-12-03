package Plugins.ClusterSystem.Permission.PermissionMessages

import Globals.GlobalVariables.serviceCode
import Plugins.ClusterSystem.Extension.ClusterEventGenerator.sendAction
import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages.MSRegisterMessage
import Plugins.ClusterSystem.Permission.{PermissionMessage, PermissionTable, permissionSchema, tokenMap}
import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.CommonUtils.TypedSystem.EventGenerator.dbWriteActionSeq
import monix.eval.Task
import slick.jdbc.PostgresProfile.api._

case class PermissionDBInitMessage() extends PermissionMessage[String] with APIPlanner[PermissionDBInitMessage]{
  /** api的执行内容 */
  override protected def plan(api: PermissionDBInitMessage)(implicit uuid:PlanUUID): Task[String] = {
    val initString="PermissionInit"
    dbWriteActionSeq(List(sql"CREATE SCHEMA IF NOT EXISTS #${permissionSchema.get}".as[Long],
            PermissionTable.permissionTable.schema.createIfNotExists)).flatMap{_=>
      sendAction(MSRegisterMessage(service = serviceCode))
    }.flatMap{registerCode=>
      sendAction(MSRegisterMessage(registerCode, service = serviceCode))
    }.flatMap{_=>
      tokenMap = Map()
      Task("")
    }
  }
}
