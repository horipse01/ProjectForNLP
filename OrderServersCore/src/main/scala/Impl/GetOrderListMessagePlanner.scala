package Impl

import Plugins.CommonUtils.TypedSystem.API.{APIPlanner, PlanUUID}
import Plugins.OrderServersApi.GetOrderListMessage
import Plugins.OrderServersShared.OrderInfo
import Tables.RentOrdertable
import monix.eval.Task

object GetOrderListMessagePlanner extends APIPlanner[GetOrderListMessage]{
  override protected def plan(api: GetOrderListMessage)(implicit uuid: PlanUUID): Task[List[OrderInfo]] = {
    RentOrdertable.getOrderInfoByUserToken(api.userToken)
  }
}