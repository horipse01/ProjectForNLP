package Plugins.CommonUtils.TypedSystem.API

import Plugins.CommonUtils.Types.TaskImplicits
import Plugins.CommonUtils.Utils.DBUtils.db
import Plugins.CommonUtils.Utils.SchedulerUtils.defaultComputation
import com.typesafe.scalalogging.Logger
import monix.eval.Task
import slick.dbio.DBIO
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.duration.{DurationLong, FiniteDuration}


trait APIPlanner[A<:API] extends TaskImplicits {
  /** 设置一下该任务的最长执行时间 */
  val executionTimeLimit: FiniteDuration= 30.seconds

  /** 获取API的实现计划，加上了计时功能 */
  final def getPlan(api:A): Task[A#ReturnType] = {
    plan(api)(api.uuid).timeout(executionTimeLimit)
  }

  /** api的执行内容 */
  protected def plan(api:A)(implicit uuid:PlanUUID): Task[A#ReturnType]

  @deprecated(message = "use Plugins.CommonUtils.Utils.IOUtils.logger", since = "后端 planner 内部方法打印日志")
  implicit def logger(implicit uuid: PlanUUID): Logger = Logger(this.getClass.getSimpleName + "-" + uuid.id)

}
