package Tables
import slick.jdbc.PostgresProfile.api._
import Plugins.CommonUtils.ServiceCenter
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import slick.lifted.{ProvenShape, Tag, TableQuery}
import Plugins.CommonUtils.TypedSystem.EventGenerator.{dbReadAction, dbReadActionSeq, dbWriteAction}
import monix.eval.Task

case class UserTableRow(
                       userName:String,
                       email:String,
                       password:String,
                       userToken:String,
                       studentId:String,
                       //加一个自动增加的id：
                       id:Int = 0
                       )

class UserTable(tag:Tag) extends Table[UserTableRow](tag, ServiceCenter.mainSchema, _tableName = "user") {
  def userName: Rep[String] = column[String]("user_name", O.PrimaryKey)
  def email: Rep[String] = column[String]("email")
  def password: Rep[String] = column[String]("password")
  def userToken: Rep[String] = column[String]("user_token")

  //加入新的字段：学号
  def studentId: Rep[String] = column[String]("student_id")

  //加一个自动增加的id：
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc, O.SqlType("SERIAL"))
  //重写有学号版本的以下所有方法
  override def * :ProvenShape[UserTableRow] = (userName, email, password, userToken, studentId, id).mapTo[UserTableRow]
}

object UserTable {
  //重写有学号版本的以下所有代码，原文：val userTable = TableQuery[UserTable]
  val userTable = TableQuery[UserTable]
  //重写有学号版本的以下所有代码，原文：def addEntry(userName: String, email: String, password: String, userToken: String)(implicit uuid: PlanUUID): Task[Int] = {
  def addEntry(userName: String, email: String, password: String, userToken: String, studentId: String)(implicit uuid: PlanUUID): Task[Int] = {
    dbWriteAction(
    //重写有学号版本的以下所有代码，原文：  userTable += UserTableRow(userName, email, password, userToken)
      userTable += UserTableRow(userName, email, password, userToken, studentId)
    )
  }

  //改成用学号和密码def checkPassword(userName: String, password: String)(implicit uuid: PlanUUID): Task[String] = {
  def checkPassword(studentId: String, password: String)(implicit uuid: PlanUUID): Task[String] = {
    //重写有学号版本的以下所有代码，原文：dbReadAction(
    dbReadAction(
      //用学号userTable.filter(r => r.userName === userName && r.password === password).map(_.userToken).result.headOption
      userTable.filter(r => r.studentId === studentId && r.password === password).map(_.userToken).result.headOption
    ).map(_.get)
  }

  //加一个改密码的函数
  def changePassword(studentId: String, password: String)(implicit uuid: PlanUUID): Task[Int] = {
    dbWriteAction(
      userTable.filter(r => r.studentId === studentId).map(_.password).update(password)
    )
  }

}