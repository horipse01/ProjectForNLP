package Plugins.CommonUtils.Types

import Plugins.CommonUtils.Utils.DBUtils.db
import monix.eval.Task
import slick.dbio.DBIO
import monix.execution.Scheduler.Implicits.global
import slick.jdbc.PostgresProfile.api._

trait TaskImplicits {
  implicit class TaskOps[T](task: Task[T]) {
    def toDBIO: DBIO[T] = DBIO.from(task.runToFuture)
  }

  implicit class DBIOOps[T](query: DBIO[T]) {
    def toTask: Task[T] = Task.deferFuture(db.run(query.transactionally))
  }
}

object TaskImplicits extends TaskImplicits