package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Tables.CarInfoTable
import monix.eval.Task
import Plugins.CarInfoServersApi.AddCarInfoMessage

object AddCarInfoMessagePlanner extends APIPlanner[AddCarInfoMessage]{
  override protected def plan(api: AddCarInfoMessage)(implicit uuid: PlanUUID): Task[String] = {
    CarInfoTable.addEntry(api.model, api.color, api.rentPrice, api.availability, api.imageUrl) >>
      Task.now("Car info added successfully")
  }
}