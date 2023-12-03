package Plugins.CommonUtils.TypedSystem.Actor

import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.RootBehavior.{AkkaThreadInfo, RootCommand, ThreadFinish, ThreadName}
import Plugins.CommonUtils.TypedSystem.sendError
import Plugins.CommonUtils.Utils.IOUtils
import Plugins.CommonUtils.Utils.IOUtils.passwordRemoval
import akka.actor.typed.scaladsl.{ActorContext, Behaviors, StashBuffer}
import akka.actor.typed.{ActorRef, Behavior}
import com.typesafe.scalalogging.Logger
import monix.execution.Scheduler.Implicits.global

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

abstract class ActorBase[A<:ActorAPI](name:String, dependencies:List[ThreadName])(implicit classTag:ClassTag[A]) {
  protected trait ActorCommand
  protected case class APIWrap(api:A) extends ActorCommand
  protected case class FinishedTask() extends ActorCommand

  val threadName: ThreadName =ThreadName(name)
  var handler: ActorRef[A] = _

  def threadInfo: AkkaThreadInfo = AkkaThreadInfo(setter, threadName, dependencies)

  def setter(root: ActorContext[RootCommand]): Unit = {
    root.spawn(apply(root), threadName.name)
    afterInit()
  }
  def init(ctx:ActorContext[ActorCommand]):Unit={}
  def afterInit():Unit={}

  def apply(root: ActorContext[RootCommand]): Behavior[ActorCommand] =
    Behaviors.withStash(1000) { buffer =>
      Behaviors.setup[ActorCommand] { ctx => {
        Try {
          handler = ctx.messageAdapter[A]((msg: A) =>  APIWrap(msg))
          val logger: Logger = Logger(ctx.self.path.name)
          Try(init(ctx)).failed.map(_.printStackTrace())
          root.self ! ThreadFinish(threadName)
          working(ctx, buffer, logger)
        } match {
          case Success(value) =>
            value
          case Failure(exception) =>
            sendError(exception, PlanUUID("ActorBaseError"))
            Behaviors.same
        }
      }
      }
    }

  def working(context: ActorContext[ActorCommand], buffer: StashBuffer[ActorCommand], logger: Logger):Behavior[ActorCommand]={
    Behaviors.receiveMessage {
      case APIWrap(api:A) =>
        logger.info(s"收到消息${passwordRemoval(IOUtils.serialize(api))}，准备处理")
        context.pipeToSelf(api.action(api.uuid).runToFuture){ reply=>
          if (api.hasReply) api.sender.get ! reply
          else reply.failed.map(sendError(_, api.uuid))
          FinishedTask()
        }
        waiting(context, buffer,logger)
      case other=>
        buffer.stash(other)
        Behaviors.same
    }
  }
  def waiting(context:ActorContext[ActorCommand],buffer: StashBuffer[ActorCommand], logger: Logger):Behavior[ActorCommand]={
    Behaviors.receiveMessage {
      case _: FinishedTask=>
        buffer.unstashAll(working(context,buffer, logger))
      case other=>
        buffer.stash(other)
        Behaviors.same
    }
  }
}
