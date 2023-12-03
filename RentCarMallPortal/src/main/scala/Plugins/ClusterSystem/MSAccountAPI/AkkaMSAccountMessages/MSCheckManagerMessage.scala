package Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages

import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessage

case class MSCheckManagerMessage(service : String, userToken : String) extends AkkaMSAccountMessage {
}
