package Plugins.OrderServersShared

import Plugins.CommonUtils.Types.JacksonSerializable

case class OrderInfo(
  orderID:Int,
  userToken:String,
  carID:Int,
  model:String,
  rentDays:Int,
  rentPrice:Float,
  
) extends JacksonSerializable