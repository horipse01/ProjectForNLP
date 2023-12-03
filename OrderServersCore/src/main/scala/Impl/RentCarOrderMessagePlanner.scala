package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.OrderServersApi.RentCarOrderMessage
import Tables.RentOrdertable
import monix.eval.Task

object RentCarOrderMessagePlanner extends APIPlanner[RentCarOrderMessage]{
  override protected def plan(api: RentCarOrderMessage)(implicit uuid: PlanUUID): Task[String] = {
    RentOrdertable.addEntry(api.userToken, api.carID, api.model, api.rentDays, api.rentPrice) >>
      Task.now("Order successfully placed.")
  }
}