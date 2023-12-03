package Plugins

import Plugins.ClusterSystem.ErrorLog.ErrorHandler
import Plugins.ClusterSystem.Extension.ClusterRootBehavior
import Plugins.CommonUtils.TypedSystem.RootBehavior.{AkkaThreadInfo, RootCommand}
import Plugins.CommonUtils.TypedSystem.{akkaSystem, config, sendError}
import akka.actor.typed.ActorSystem
import akka.util.Timeout

import java.util.concurrent.TimeUnit

package object ClusterSystem{
  val askTimeOut: Timeout = Timeout(10, TimeUnit.SECONDS)
  def setClusterSystem(akkaThreads:List[AkkaThreadInfo], beforeInit:()=>Unit=()=>{}, afterInit:()=>Unit=()=>{}):Unit= {
    sendError= ErrorHandler.send
    akkaSystem=ActorSystem[RootCommand](ClusterRootBehavior.apply(akkaThreads,beforeInit, afterInit), "QianFangCluster", config)
  }
}
