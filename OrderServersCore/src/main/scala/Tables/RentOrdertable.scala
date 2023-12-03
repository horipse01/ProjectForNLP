package Tables

import Plugins.OrderServersShared.OrderInfo
import Plugins.CommonUtils.ServiceCenter
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.EventGenerator.{dbReadAction, dbWriteAction}
import monix.eval.Task
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery}

case class RentOrderRow(
                       userToken:String,
                       carID:Int,
                       model:String,
                       rentDays:Int,
                       rentPrice:Float,
                       orderID:Int = 0,
                       )

class RentOrdertable(tag:Tag) extends Table[RentOrderRow](tag,ServiceCenter.mainSchema,_tableName = "rent_order") {
  def userToken:Rep[String] = column[String]("user_token")
  def carID:Rep[Int] = column[Int]("car_id")
  def model:Rep[String] = column[String]("model")
  def rentDays:Rep[Int] = column[Int]("rent_days")
  def rentPrice:Rep[Float] = column[Float]("rent_price")
  def orderID:Rep[Int] = column[Int]("order_id",O.PrimaryKey,O.AutoInc,O.SqlType("SERIAL"))
  def * :ProvenShape[RentOrderRow] = (userToken,carID,model,rentDays,rentPrice,orderID).mapTo[RentOrderRow]
}

object RentOrdertable{
  val rentOrderTable = TableQuery[RentOrdertable]
  def addEntry(userToken:String,carID:Int,model:String,rentDays:Int,rentPrice:Float)(implicit uuid:PlanUUID):Task[Int]= {
    dbWriteAction(
      rentOrderTable += RentOrderRow( userToken, carID, model, rentDays, rentPrice)
    )
  }
  def getOrderInfoByUserToken(userToken:String)(implicit uuid:PlanUUID):Task[List[OrderInfo]] = {
    dbReadAction(rentOrderTable.filter(_.userToken === userToken).result).map(_.map(r=>{
      OrderInfo(r.orderID,r.userToken,r.carID,r.model,r.rentDays,r.rentPrice)
    }).toList)
  }
}