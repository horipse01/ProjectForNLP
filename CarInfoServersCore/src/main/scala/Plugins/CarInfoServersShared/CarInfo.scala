package Plugins.CarInfoServersShared

import Plugins.CommonUtils.Types.JacksonSerializable

case class CarInfo(
  carID:Int,
  model:String,
  color:String,
  rentPrice:Float,
  availability:Boolean,
  imageUrl:String,
  
) extends JacksonSerializable