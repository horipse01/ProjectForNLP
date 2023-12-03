package Plugins.CommonUtils.TypedSystem

import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.Utils.DBUtils.db
import monix.eval.Task
import slick.dbio
import slick.dbio.{DBIO, NoStream}
import slick.sql.SqlAction

object EventGenerator {
  /** 数据库操作做成异步，返回ReplyMessage给listener。我们专门产生一个DBRunner Actor来做这个事情 */
  def dbReadActionSeq[T, S <: NoStream, E <: dbio.Effect.Read](actions: List[SqlAction[T, S, E]])(implicit uuid:PlanUUID): Task[List[T]] =
    Task.deferFuture {
      db.run(DBIO.sequence(actions))
    }

  /** 数据库操作做成异步，返回ReplyMessage给listener。我们专门产生一个DBRunner Actor来做这个事情 */
  def dbWriteActionSeq[T, S <: NoStream, E <: dbio.Effect.Write](actions: List[SqlAction[T, S, E]])(implicit uuid:PlanUUID): Task[List[T]] =
    Task.deferFuture {
      db.run(DBIO.sequence(actions))
    }

  /** 如果action不是一个list，那么改成一个list发过去 */
  def dbReadAction[T, S <: NoStream, E <: dbio.Effect.Read](action: SqlAction[T, S, E])(implicit uuid:PlanUUID): Task[T] = {
    Task.deferFuture {
      db.run(action)
    }
  }

  /** 如果action不是一个list，那么改成一个list发过去 */
  def dbWriteAction[T, S <: NoStream, E <: dbio.Effect.Write](action: SqlAction[T, S, E])(implicit uuid:PlanUUID): Task[T] =
    Task.deferFuture {
      db.run(action)
    }
}
