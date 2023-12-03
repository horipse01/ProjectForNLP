package Plugins.CommonUtils.TypedSystem.Actor

import Plugins.CommonUtils.TypedSystem.API.{API, PlanUUID}
import akka.actor.typed.ActorRef
import monix.eval.Task

import scala.util.Try


abstract class ActorAPI() extends API{

  val sender:Option[ActorRef[Try[ReturnType]]]
  /** 当前Actor处理的内容 */
  def action(implicit uuid:PlanUUID):Task[ReturnType]

  override def hasReply: Boolean = sender.isDefined
}

