package Plugins.ClusterSystem

import Globals.GlobalVariables.serviceCode
import Plugins.CommonUtils.ServiceCenter.serviceName

package object Permission {
  val permissionSchema: Option[String] = Option(serviceName(serviceCode).replace('-', '_') + "_permission")
  var tokenMap: Map[String, String] = _
}
