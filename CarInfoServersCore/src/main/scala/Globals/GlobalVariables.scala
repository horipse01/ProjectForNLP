package Globals

import Plugins.CommonUtils.TypedSystem.RootBehavior.AkkaThreadInfo
import Utils.DBUtils
import Plugins.CarInfoServersApi.AkkaCarInfoServersMessage
import Plugins.CommonUtils.ServiceCenter.carInfoServersServiceCode

object GlobalVariables {

  /** ******************************* Threads ******************************** */
  var beforeSystemInit: () => Unit = () => {
    DBUtils.initDatabase()
  }

  val threads: List[AkkaThreadInfo] = List()

  var afterSystemInit: () => Unit = () => {
  }

  lazy val serviceCode: String = carInfoServersServiceCode

  type APIType = AkkaCarInfoServersMessage
}
