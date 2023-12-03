package Globals

import Plugins.ClusterSystem.Extension.ClusterAPI
//import Plugins.CommonUtils.ServiceCenter.rentCarMallPortalServiceCode
import Plugins.CommonUtils.ServiceCenter._
import Plugins.CommonUtils.TypedSystem.RootBehavior.AkkaThreadInfo
import Plugins.CommonUtils.TypedSystem.akkaSystem
import Process.PortalHttpServer.startHttpServer
import Process.Routes

object GlobalVariables {

  type APIType = ClusterAPI

  
  lazy val serviceCode: String = rentCarMallPortalServiceCode

  private def getServiceCode(serviceCodePrefix: String): String = {
    if (serviceCodePrefix.endsWith("Core")){
      serviceCodePrefix.reverse.replaceFirst("Core", "").reverse
    }else{
      serviceCodePrefix
    } + "ServiceCode"
  }


  /** ******************************************** threads ********************************************* */

  var beforeSystemInit: () => Unit = () => {
  }

  val threads: List[AkkaThreadInfo] = List(

  )

  var afterSystemInit: () => Unit = () => {
    startHttpServer(new Routes()(akkaSystem).tongWenPortalRoutes, akkaSystem)
  }
}
