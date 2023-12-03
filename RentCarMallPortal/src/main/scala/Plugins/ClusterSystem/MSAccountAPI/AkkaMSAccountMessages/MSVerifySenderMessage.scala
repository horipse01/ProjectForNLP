package Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages

import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessage

case class MSVerifySenderMessage(src : String, sink : String, token : String) extends AkkaMSAccountMessage
