package Tables

import monix.eval.Task
import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._
import Plugins.CommonUtils.ServiceCenter
import slick.lifted.{ProvenShape, Tag}
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.EventGenerator.{dbReadAction, dbReadActionSeq, dbWriteAction}
import monix.eval.Task
import slick.jdbc.PostgresProfile.api._
import slick.lifted.{ProvenShape, TableQuery}
import Plugins.CarInfoServersShared.CarInfo

case class CarInfoRow(
              model:String,
              color:String,
              rentPrice:Float,
              availability:Boolean,
              imageUrl:String,
              carID:Int = 0
              )

class CarInfoTable(tag:Tag) extends Table[CarInfoRow](tag,ServiceCenter.mainSchema,_tableName = "car_info") {
  def model:Rep[String] = column[String]("model")
  def color:Rep[String] = column[String]("color")
  def rentPrice:Rep[Float] = column[Float]("rent_price")
  def availability:Rep[Boolean] = column[Boolean]("availability")
  def imageUrl:Rep[String] = column[String]("image_url")
  def carID:Rep[Int] = column[Int]("car_id",O.PrimaryKey,O.AutoInc,O.SqlType("SERIAL"))
  def * :ProvenShape[CarInfoRow] = (model,color,rentPrice,availability,imageUrl,carID).mapTo[CarInfoRow]
}

object CarInfoTable {
  val carInfoTable = TableQuery[CarInfoTable]

  def addEntry(model:String,color:String,rentPrice:Float,availability:Boolean,imageUrl:String)(implicit uuid:PlanUUID):Task[Int]= {
    dbWriteAction(
      carInfoTable += CarInfoRow(model,color,rentPrice,availability,imageUrl)
    )
  }

  def getCarInfolist()(implicit uuid: PlanUUID): Task[List[CarInfo]] = {
    dbReadAction(carInfoTable.result).map(_.map(r=>{
      CarInfo(r.carID,r.model,r.color,r.rentPrice,r.availability,r.imageUrl)
    }).toList)
  }
}