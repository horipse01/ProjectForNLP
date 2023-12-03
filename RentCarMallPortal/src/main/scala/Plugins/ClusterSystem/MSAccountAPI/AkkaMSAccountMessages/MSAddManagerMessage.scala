package Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages

import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessage

case class MSAddManagerMessage(service : String, userToken : String, supervisorToken : String) extends AkkaMSAccountMessage
