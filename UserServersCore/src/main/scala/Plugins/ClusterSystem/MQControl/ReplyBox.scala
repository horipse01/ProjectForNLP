package Plugins.ClusterSystem.MQControl

import Plugins.ClusterSystem.Exceptions.ReplyMessageUUIDRouteNotFoundException
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.sendError
import Plugins.CommonUtils.Types.ReplyMessage
import Plugins.CommonUtils.Utils.IOUtils
import akka.actor.typed.scaladsl.{Behaviors, TimerScheduler}
import akka.actor.typed.{ActorRef, Behavior, SupervisorStrategy}
import org.joda.time.DateTime

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}
import scala.util.{Failure, Success, Try}

private[MQControl] sealed trait ReplyBoxCommand


/** 远处的微服务处理请求完毕，返回了一个replyMessage */
case class NewRequest(time: Long, uuid: PlanUUID, ref: ActorRef[ReplyMessage]) extends ReplyBoxCommand

/** 远处的微服务处理请求完毕，返回了一个replyMessage */
case class NewReply(replyMessage: ReplyMessage, deliveryAction: () => Unit) extends ReplyBoxCommand

/** 清除掉一定时间之内尚未收到回复的API记录 */
case class RemoveUUIDRef(uuid: PlanUUID) extends ReplyBoxCommand

object ReplyBox {

  val namePrefix = "replyBox"
  implicit val uuid: PlanUUID = PlanUUID(namePrefix)

  private var uuidRefMapBackup: Map[String, ActorRef[ReplyMessage]] = Map()

  def workBehavior(timers: TimerScheduler[ReplyBoxCommand],
                   uuidRefMap: Map[String, ActorRef[ReplyMessage]]
                  ): Behavior[ReplyBoxCommand] = {

    Behaviors.receiveMessage[ReplyBoxCommand] { command =>
      Try {
        command match {
          case NewRequest(time, uuid, ref) =>
            /** 检查是否超时，否则设置一个闹钟，准备processTimeOut之后把这个请求相关信息清空，防止map越来越大 */
            val currentTime = DateTime.now().getMillis
            if (time + processTimeOut.toMillis > currentTime)
              timers.startSingleTimer(RemoveUUIDRef(uuid), FiniteDuration.apply(time + processTimeOut.toMillis - currentTime, MILLISECONDS))
            workBehavior(timers, uuidRefMap + (uuid.id -> ref))
          case NewReply(replyMessage, deliveryAction) =>
            /** 收到回复，首先给一个回应 */
            deliveryAction()
            /** 从map中找到之前记录的sender */
            val replyTo = uuidRefMap.getOrElse(replyMessage.uuid, throw ReplyMessageUUIDRouteNotFoundException(replyMessage.uuid))
            /** 把message发给sender */
            replyTo ! replyMessage

            /** 把相关信息从map中清除 */
            workBehavior(timers, uuidRefMap - replyMessage.uuid)
          case RemoveUUIDRef(uuid) =>
            /** 把相关信息从map中清除 */
            workBehavior(timers, uuidRefMap - uuid.id)
        }
      } match {
        case Success(value) =>
          value
        case Failure(exception) =>
          sendError(exception, uuid)
          Behaviors.same
      }
    }.receiveSignal {
      case (_, akka.actor.typed.PreRestart) =>
        //重启时只备份一下initialUUIDRefMap，定时器和消息都丢掉了，因为每个message都是异步处理，所以mailBox也不会积攒很多消息，问题不大
        uuidRefMapBackup = uuidRefMap
        IOUtils.printWarning("replyBox重启！")
        Behaviors.same
    }
  }

  def apply(parent: ActorRef[RabbitWorkerCommand]): Behavior[ReplyBoxCommand] =
    Behaviors.supervise {
      Behaviors.setup[ReplyBoxCommand] { ctx =>
        parent ! FinishedBox(ctx.self.path.name)
        Behaviors.withTimers[ReplyBoxCommand] {
          workBehavior(_, uuidRefMapBackup)
        }
      }
    }.onFailure(SupervisorStrategy.restart.withStashCapacity(1000))


}

