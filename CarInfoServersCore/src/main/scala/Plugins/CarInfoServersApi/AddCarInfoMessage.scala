package Plugins.CarInfoServersApi

import Plugins.CarInfoServersApi.MSAkkaCarInfoServersMessageExtended


case class AddCarInfoMessage(
  model:String,
  color:String,
  rentPrice:Float,
  availability:Boolean,
  imageUrl:String,
) extends MSAkkaCarInfoServersMessageExtended[String]
