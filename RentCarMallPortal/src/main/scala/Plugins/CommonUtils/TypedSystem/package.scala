package Plugins.CommonUtils

import Plugins.CommonUtils.ServiceCenter.servicePort
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.RootBehavior.{AkkaThreadInfo, LocalRootBehavior, RootCommand}
import Plugins.CommonUtils.UserPath.chosenPath
import akka.actor.typed.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

package object TypedSystem {
  var akkaSystem: ActorSystem[RootCommand] = _
  var sendError: (Throwable,PlanUUID)=>Unit = _

  lazy val config: Config = ConfigFactory
    .parseString(
      s"""
      akka.remote.artery.canonical.port=$servicePort,
      akka.remote.artery.canonical.hostname=${chosenPath.akkaServerHostName()},
      akka.remote.artery.bind.hostname=${"0.0.0.0"},
      akka.remote.artery.bind.port=$servicePort,
      akka.cluster.seed-nodes=[${chosenPath.seedNodeName()}],
      """
    )withFallback(ConfigFactory.load())

  def setLocalSystem(akkaThreads:List[AkkaThreadInfo], beforeInit:()=>Unit=()=>{}, afterInit:()=>Unit=()=>{}):Unit= {
    sendError= LocalErrorHandler.send
    akkaSystem=ActorSystem[RootCommand](LocalRootBehavior(akkaThreads, beforeInit, afterInit), "QianFangCluster", config)
  }
}
