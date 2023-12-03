package Plugins.CarInfoServersApi

import Plugins.CarInfoServersApi.MSAkkaCarInfoServersMessageExtended
import Plugins.CarInfoServersShared.CarInfo

case class GetCarInfoMessage(
  
) extends MSAkkaCarInfoServersMessageExtended[List[CarInfo]]
