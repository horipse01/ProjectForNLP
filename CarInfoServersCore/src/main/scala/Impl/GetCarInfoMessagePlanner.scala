package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.CarInfoServersApi.GetCarInfoMessage
import Plugins.CarInfoServersShared.CarInfo
import Tables.CarInfoTable
import monix.eval.Task

object GetCarInfoMessagePlanner extends APIPlanner[GetCarInfoMessage] {
  override protected def plan(api: GetCarInfoMessage)(implicit uuid: PlanUUID): Task[List[CarInfo]] = {
    CarInfoTable.getCarInfolist()
  }
}