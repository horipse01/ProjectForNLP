package Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages

import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessage

case class MSRegisterMessage(registerCode : String = "", service : String) extends AkkaMSAccountMessage[String]