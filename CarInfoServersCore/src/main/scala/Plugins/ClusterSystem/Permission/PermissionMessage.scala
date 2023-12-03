package Plugins.ClusterSystem.Permission

import Plugins.ClusterSystem.Extension.ClusterAPIExtended
import Plugins.ClusterSystem.Permission.PermissionMessages._
import Plugins.ClusterSystem.ToClusterMessages.MQRoute
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

import scala.reflect.runtime.universe._


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[AddMessageTypeMessage], name = "AddMessageTypeMessage"),
    new JsonSubTypes.Type(value = classOf[CheckAllMessageTypeMessage], name = "CheckAllMessageTypeMessage"),
    new JsonSubTypes.Type(value = classOf[CheckAllowListMessage], name = "CheckAllowListMessage"),
    new JsonSubTypes.Type(value = classOf[UpdateAllowListMessage], name = "UpdateAllowListMessage"),
    new JsonSubTypes.Type(value = classOf[PermissionDBInitMessage], name = "PermissionDBInitMessage"),
  )
)
abstract class PermissionMessage[Ret:TypeTag](val managerToken : String = "", val forwardService : String = "") extends ClusterAPIExtended[Ret]{
  override def getRoute: MQRoute = MQRoute("")
}
