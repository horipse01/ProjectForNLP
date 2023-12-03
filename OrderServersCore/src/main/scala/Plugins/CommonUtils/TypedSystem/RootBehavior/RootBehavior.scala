package Plugins.CommonUtils.TypedSystem.RootBehavior

import Plugins.CommonUtils.Exceptions.SameThreadNameException
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.TypedSystem.sendError
import Plugins.CommonUtils.Utils.IOUtils
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import com.typesafe.scalalogging.Logger

import scala.util.{Failure, Success, Try}

sealed trait RootCommand

/** 某个Thread初始化成功了 */
case class ThreadFinish(threadName: ThreadName) extends RootCommand


/** Actor系统的root结点，每次先初始化这个Actor，然后再调用拓扑排序调用其他Actor */
abstract class RootBehavior {
  def getPlanUUID: PlanUUID = PlanUUID("RootBehavior")
  val logger: Logger = IOUtils.logger(getPlanUUID)

  /** 已完成的、进行中的、未完成（等待中）的 */
  def topologicalInit(finished: Set[ThreadName], processing: Set[ThreadName], waiting: Set[ThreadName], ctx: ActorContext[RootCommand],
                      akkaThreads: List[AkkaThreadInfo], afterInit: () => Unit): Behavior[RootCommand] =
    Behaviors.receiveMessage[RootCommand] { command =>
      Try {
        command match {
          case ThreadFinish(key) =>

            val newFinished = finished + key
            var newProcessing = processing - key
            val newWaiting = waiting - key

            /** 拓扑排序，运行尚未运行并且依赖已经初始化成功的Threads */
            if (newWaiting.nonEmpty) {
              val newThreads = akkaThreads.filter(thread => newWaiting.contains(thread.threadName) &&
                (!newProcessing.contains(thread.threadName))
                && thread.dependencies.forall(newFinished.contains))
              newThreads.foreach(
                a => {
                  logger.info(s"启动${a.threadName.name}中...")
                  newProcessing = newProcessing + a.threadName
                  a.setter.apply(ctx)
                  logger.info(s"启动${a.threadName.name}结束")
                }
              )
              topologicalInit(newFinished, newProcessing, newWaiting, ctx, akkaThreads, afterInit)
            } else {
              /** 如果没有尚未运行的Thread，则运行最后的扫尾部分 */
              Try(afterInit()).failed.map(e => logger.error("afterInit error, e = ", e))
              Behaviors.empty[RootCommand]
            }
          case _ => Behaviors.same[RootCommand]
        }
      } match {
        case Failure(exception) =>
          sendError(exception, getPlanUUID)
          Behaviors.same
        case Success(value) => value
      }
    }

  def extraInit(): Unit = {}

  def extraSetup(initThreads: List[AkkaThreadInfo]): List[AkkaThreadInfo] = initThreads

  def apply(initThreads: List[AkkaThreadInfo], beforeInit: () => Unit = () => {}, afterInit: () => Unit = () => {}): Behavior[RootCommand] = Behaviors.setup[RootCommand] { ctx =>

    Try {
      /** 加入各种threads */
      val logger = Logger(ctx.self.path.name)

      logger.info("Actor System启动前初始化...")
      Try(beforeInit()).failed.map(e => logger.error("beforeInit error, e = ", e))
      Try(extraInit()).failed.map(e => logger.error("extraInit error, e = ", e))
      logger.info("Actor System启动前初始化结束。")

      val akkaThreads: List[AkkaThreadInfo] = extraSetup(initThreads)
      val names = akkaThreads.map(_.threadName.name)

      /** 我们要求ThreadName不可以一样，因为这是每个Thread的唯一标识 */
      if (names.length != names.distinct.length)
        throw SameThreadNameException()
      else {
        logger.info("正在按顺序启动Akka Threads...")
        var processing: Set[ThreadName] = Set[ThreadName]()
        akkaThreads.filter(_.dependencies.isEmpty).foreach(a => {
          logger.info(s"启动${a.threadName.name}中...")
          processing = processing + a.threadName
          Try(a.setter.apply(ctx)).failed.map(_.printStackTrace())
          logger.info(s"启动${a.threadName.name}结束")
        })
        topologicalInit(Set[ThreadName](), processing, akkaThreads.map(_.threadName).toSet, ctx, akkaThreads, afterInit)
      }
    } match {
      case Failure(exception) =>
        exception.printStackTrace()
        sendError(exception, getPlanUUID)
        Behaviors.same
      case Success(value) => value
    }
  }
}
