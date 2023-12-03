package Plugins.ClusterSystem.MSAccountAPI

import Plugins.ClusterSystem.Extension.ClusterAPIExtended
import Plugins.ClusterSystem.MSAccountAPI.AkkaMSAccountMessages.{MSAddManagerMessage, MSCheckManagerMessage, MSRegisterMessage, MSVerifySenderMessage}
import Plugins.ClusterSystem.ToClusterMessages.{MQRoute, ToMSAccountMessage}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

import scala.reflect.runtime.universe._

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[MSAddManagerMessage], name = "MSAddManagerMessage"),
    new JsonSubTypes.Type(value = classOf[MSCheckManagerMessage], name = "MSCheckManagerMessage"),
    new JsonSubTypes.Type(value = classOf[MSRegisterMessage], name = "MSRegisterMessage"),
    new JsonSubTypes.Type(value = classOf[MSVerifySenderMessage], name = "MSVerifySenderMessage"),
  )
)
abstract class AkkaMSAccountMessage[Ret:TypeTag] extends ClusterAPIExtended[Ret]{
  override def getRoute: MQRoute= ToMSAccountMessage.msAccountRoute
}
