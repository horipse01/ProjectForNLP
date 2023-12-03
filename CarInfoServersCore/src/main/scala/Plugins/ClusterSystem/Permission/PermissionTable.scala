package Plugins.ClusterSystem.Permission

import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.EventGenerator.{dbReadAction, dbWriteAction}
import Plugins.CommonUtils.Types.CustomColumnTypes._
import Plugins.CommonUtils.Types.JacksonSerializable
import monix.eval.Task
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, Tag}

case class PermissionRow(messageType : Int, allowList : List[String]) extends JacksonSerializable

class PermissionTable(tag : Tag) extends Table[PermissionRow](tag, permissionSchema, "permission") {
  def messageType: Rep[Int] = column[Int]("message_type", O.PrimaryKey)
  def allowList: Rep[List[String]] = column[List[String]]("allow_list")
  def * : ProvenShape[PermissionRow] = (messageType, allowList).mapTo[PermissionRow]
}

object PermissionTable {
  val permissionTable = TableQuery[PermissionTable]

  def checkAllowList(messageType : Int)(implicit uuid:PlanUUID): Task[Option[List[String]]] =dbReadAction(
    permissionTable.filter(_.messageType === messageType).map(_.allowList).result.headOption
  )

  def addRow(row:PermissionRow)(implicit uuid:PlanUUID): Task[Int] =dbWriteAction(
    permissionTable+=row
  )


  def updateAllowList(messageType : Int, allowList : List[String])(implicit uuid:PlanUUID): Task[Int] =dbWriteAction(
    permissionTable.filter(_.messageType === messageType).map(_.allowList).update(allowList)
  )

  def checkAllMessageType()(implicit uuid:PlanUUID): Task[Seq[PermissionRow]] =dbReadAction(
    permissionTable.result
  )
}
