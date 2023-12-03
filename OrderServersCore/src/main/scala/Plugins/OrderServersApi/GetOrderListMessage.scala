package Plugins.OrderServersApi

import Plugins.OrderServersApi.MSAkkaOrderServersMessageExtended
import Plugins.OrderServersShared.OrderInfo

case class GetOrderListMessage(
  userToken:String,
) extends MSAkkaOrderServersMessageExtended[List[OrderInfo]]
