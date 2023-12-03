package Plugins.OrderServersApi

import Plugins.OrderServersApi.MSAkkaOrderServersMessageExtended


case class RentCarOrderMessage(
  userToken:String,
  carID:Int,
  model:String,
  rentDays:Int,
  rentPrice:Float,
) extends MSAkkaOrderServersMessageExtended[String]
