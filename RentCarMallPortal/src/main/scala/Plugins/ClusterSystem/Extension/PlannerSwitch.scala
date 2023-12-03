package Plugins.ClusterSystem.Extension

import Plugins.ClusterSystem.Exceptions.APIMessageExecutorNotFoundException
import Plugins.CommonUtils.TypedSystem.API.{API, APIPlanner}
import com.typesafe.scalalogging.Logger

import java.util.concurrent.ConcurrentHashMap
import scala.reflect.runtime.universe
import scala.util.{Failure, Success, Try}

/** 根据API的不同选择不同的Planner。这是在Handler里面用到的，仅用于实现ClusterAPI，即微服务之间的API。
 * 对于这样的API，我们在Plugins中定义其声明XXXAPIMessage，在Impl中定义其XXXAPIMessagePlanner，使用PlannerSwitch做好对应。 */
object PlannerSwitch {

  private val cache = new ConcurrentHashMap[String, Any]()
  private val logger = Logger(getClass.getSimpleName)

  def switch[A <: API](api: A): APIPlanner[A] = {
    val key = api.getClass.getName
    val startTime = System.currentTimeMillis()

    val APIPlanner = cache.computeIfAbsent(key, _ => {
      logger.info("PlannerSwitch#switch, getTargetInstance from reflect")
      getTargetInstance(api)
    }).asInstanceOf[APIPlanner[A]]

    logger.info("PlannerSwitch#switch, get api = {} planner cost mills = {}", api.getClass.getSimpleName, System.currentTimeMillis() - startTime)
    APIPlanner
  }

  /**
   * 获取子模块下的类路径
   *
   * @param api - submodule 路径下的 Cluster 实例, `Plugins.<submoduleName>.classPath`
   * @return submodule 路径下的类剧集  `classPath`
   *         eg:
   *         - api - Plugins.CloudSourcingAPI.AppealMessages.GetAppealMessage
   *         - return - AppealMessages.GetAppealMessage
   */
  private def getSubmoduleClassName[A <: API](api: A): String = {
    val classPathList = api.getClass.getName.split("\\.")
    val pluginsIdx = classPathList.indexOf("Plugins")

    classPathList.drop(pluginsIdx + 2).mkString(".")
  }

  /**
   * 获取目标的实例
   *
   * @param api 实例 - `Plugins.<submoduleName>.classPath`
   * @return 实例 - `Impl.classPath+Planner`
   *
   *         eg:
   *         - api: Plugins.CloudSourcingAPI.AppealMessages.GetAppealMessage
   *         - return: Impl.AppealMessages.GetAppealMessagePlanner
   */
  private def getTargetInstance[A <: API](api: A, classMirror: universe.Mirror = universe.runtimeMirror(getClass.getClassLoader), targetPackagePrefix: String = "Impl", targetObjectImplSuffix: String = "Planner"): Any = {
    val apiClassName = getSubmoduleClassName(api)
    val targetClassName = List(targetPackagePrefix, apiClassName + targetObjectImplSuffix).mkString(".")

    val staticMirrorTry = Try(classMirror.staticModule(targetClassName))
    staticMirrorTry match {
      case Success(staticMirror) =>
        val objectMirror = classMirror.reflect(classMirror.reflectModule(staticMirror).instance)
        objectMirror.instance
      case Failure(_) => throw APIMessageExecutorNotFoundException(errMsg = s"api = ${api.getClass.getName}, targetPlanner = ${targetClassName}")
    }
  }

}
