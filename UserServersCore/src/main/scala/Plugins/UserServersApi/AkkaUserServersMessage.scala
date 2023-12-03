package Plugins.UserServersApi

import Plugins.ClusterSystem.Extension.ClusterAPI
import Plugins.ClusterSystem.ToClusterMessages.{MQRoute, ToUserServersMessage}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import scala.reflect.runtime.universe._

import Plugins.UserServersApi.UseRegisterMessage
import Plugins.UserServersApi.UserLoginMessage

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
Array(
  new JsonSubTypes.Type(value = classOf[UseRegisterMessage], name = "UseRegisterMessage"),
  new JsonSubTypes.Type(value = classOf[UserLoginMessage], name = "UserLoginMessage"),
))
abstract class AkkaUserServersMessage() extends ClusterAPI{
  override def getRoute: MQRoute = ToUserServersMessage.userServersRoute
}

abstract class MSAkkaUserServersMessageExtended[Ret:TypeTag]() extends AkkaUserServersMessage{
  override type ReturnType = Ret
  override def getReturnTypeTag: TypeTag[Ret] = typeTag[Ret]
}
