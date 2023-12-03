package Tables
import slick.jdbc.PostgresProfile.api._
import Plugins.CommonUtils.ServiceCenter
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import slick.lifted.{ProvenShape, Tag, TableQuery}
import Plugins.CommonUtils.TypedSystem.EventGenerator.{dbReadAction, dbReadActionSeq, dbWriteAction}
import monix.eval.Task
import Plugins.UserServersShared.HistoryInformation
//创建拥有如下属性的HistoryTableRow类:用户id，学号，查询内容(String)，返回内容(String)，引用文件数(Int)，引用文件名（String[]），时间戳
case class HistoryTableRow(
                          userId:Int,
                          studentId:String,
                          queryContent:String,
                          returnContent:String,
                          referenceFileNum:Int,
                          referenceFileName:String,
                          timeStamp:String,
                          //加一个自动增加的id：
                          id:Int = 0
                          )

class HistoryTable(tag:Tag) extends Table[HistoryTableRow](tag, ServiceCenter.mainSchema, _tableName = "history") {
  def userId: Rep[Int] = column[Int]("user_id")

  def studentId: Rep[String] = column[String]("student_id")

  def queryContent: Rep[String] = column[String]("query_content")

  def returnContent: Rep[String] = column[String]("return_content")

  def referenceFileNum: Rep[Int] = column[Int]("reference_file_num")

  def referenceFileName: Rep[String] = column[String]("reference_file_name")

  def timeStamp: Rep[String] = column[String]("time_stamp")

  //加一个自动增加的id：
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc, O.SqlType("SERIAL"))

  override def * : ProvenShape[HistoryTableRow] = (userId, studentId, queryContent, returnContent, referenceFileNum, referenceFileName, timeStamp, id).mapTo[HistoryTableRow]
}
object HistoryTable {
    val historyTable = TableQuery[HistoryTable]
      //新方法：addEntry
      def addEntry(userId: Int, studentId: String, queryContent: String, returnContent: String, referenceFileNum: Int, referenceFileName: String, timeStamp: String)(implicit uuid: PlanUUID): Task[Int] = {
        dbWriteAction(
          historyTable += HistoryTableRow(userId, studentId, queryContent, returnContent, referenceFileNum, referenceFileName, timeStamp)
        )
      }
      //新方法：getHistory,以学号进行查询，返回一个HistoryInformation类型的List。你需要逐个给变量赋值而不是仅仅定义一下。
      def getHistory(studentId: String)(implicit uuid: PlanUUID): Task[List[HistoryInformation]] = {
        val list = dbReadAction(
          historyTable.filter(r => r.studentId === studentId).result
        ).map(_.toList)
        list.map(_.map(r => HistoryInformation(r.userId, r.studentId, r.queryContent, r.returnContent, r.referenceFileNum, r.referenceFileName, r.timeStamp)))
      }

}

