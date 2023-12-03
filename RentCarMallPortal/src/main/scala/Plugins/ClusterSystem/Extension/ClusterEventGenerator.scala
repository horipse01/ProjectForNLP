package Plugins.ClusterSystem.Extension

import Plugins.ClusterSystem.MQControl.{PortalForwardRequest, SendRequestToBox, requestBox}
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import Plugins.ClusterSystem.askTimeOut
import Plugins.CommonUtils.TypedSystem.API.{API, PlanUUID}
import Plugins.CommonUtils.TypedSystem.akkaSystem
import Plugins.CommonUtils.Types.ReplyMessage
import Plugins.CommonUtils.Utils.IOUtils.replyToResult
import Plugins.CommonUtils.Utils.{IOUtils, StringUtils}
import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import monix.eval.Task

import scala.concurrent.Future

object ClusterEventGenerator {
  /** 异步发送消息，把api发送给requestBox，同时设置好sender端口默默等回复 */
  def sendAction(api: ClusterAPI)(implicit uuid: PlanUUID): Task[api.ReturnType] = {
    Task.deferFutureAction { implicit executionContext =>
      implicit val qianFangSystem: ActorSystem[Nothing] = akkaSystem
      implicit val timeout: Timeout = askTimeOut
      val reply: Future[ReplyMessage] = requestBox.ask[ReplyMessage]((ref: ActorRef[ReplyMessage]) => {
        SendRequestToBox(
          api.withUUID(uuid.id + "-" + api.getRoute.name + "-" + api.getClass.getSimpleName + "-" + StringUtils.randomLetterString(5)), ref
        )
      })
      if (api.hasReply) {
        reply.flatMap(b => Future {
          replyToResult[api.ReturnType](b)(api.getReturnTypeTag, api.getReturnClassTag).get
        })
      } else
        Future({}).asInstanceOf[Future[api.ReturnType]]
    }
  }

  def relayMessage(api: ClusterAPI)(implicit uuid: PlanUUID): Task[String] = {
    Task.deferFutureAction { implicit executionContext =>
      implicit val qianFangSystem: ActorSystem[Nothing] = akkaSystem
      implicit val timeout: Timeout = askTimeOut
      val reply: Future[ReplyMessage] = requestBox.ask[ReplyMessage]((ref: ActorRef[ReplyMessage]) => {
        SendRequestToBox(
          api.withUUID(uuid.id + "-" + api.getRoute.name + "-" + api.getClass.getSimpleName + "-" + StringUtils.randomLetterString(5)), ref
        )
      })

      if (api.hasReply) {
        reply.flatMap(b => Future {
          replyToResult[String](b).get
        })
      } else
        Future({}).asInstanceOf[Future[String]]
    }
  }


  /** portal转发前端消息，把route和serializedMessage发送给requestBox，同时设置好sender端口默默等回复 */
  def portalForward(route: MQRoute, serializedMessage: String)(implicit uuid: PlanUUID): Task[ReplyMessage] = {
    Task.deferFutureAction { implicit executionContext =>
      implicit val qianFangSystem: ActorSystem[Nothing] = akkaSystem
      implicit val timeout: Timeout = askTimeOut

      var map = IOUtils.deserialize[Map[String, Any]](serializedMessage)
      map = map + ("uuid" -> uuid)

      requestBox.ask[ReplyMessage]((ref: ActorRef[ReplyMessage]) => {
        PortalForwardRequest(route, IOUtils.serialize(map), uuid, ref)
      })
    }
  }

}
