package Plugins.ClusterSystem.Extension

import Plugins.ClusterSystem.MQControl.RabbitWorker
import Plugins.CommonUtils.TypedSystem.RootBehavior.{AkkaThreadInfo, RootBehavior}
import Plugins.CommonUtils.UserPath

object ClusterRootBehavior extends RootBehavior {
  override def extraInit(): Unit = {
    if (UserPath.chosenPath.localAlgorithmRunner)
      RabbitWorker.anonymousInit()
    else
      RabbitWorker.init()
  }

  override def extraSetup(initThreads: List[AkkaThreadInfo]): List[AkkaThreadInfo] = initThreads :+  RabbitWorker.threadInfo
}
