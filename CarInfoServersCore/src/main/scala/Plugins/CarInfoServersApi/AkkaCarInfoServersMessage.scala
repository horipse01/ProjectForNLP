package Plugins.CarInfoServersApi

import Plugins.ClusterSystem.Extension.ClusterAPI
import Plugins.ClusterSystem.ToClusterMessages.{MQRoute, ToCarInfoServersMessage}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import scala.reflect.runtime.universe._

import Plugins.CarInfoServersApi.GetCarInfoMessage
import Plugins.CarInfoServersApi.AddCarInfoMessage

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
Array(
  new JsonSubTypes.Type(value = classOf[GetCarInfoMessage], name = "GetCarInfoMessage"),
  new JsonSubTypes.Type(value = classOf[AddCarInfoMessage], name = "AddCarInfoMessage"),
))
abstract class AkkaCarInfoServersMessage() extends ClusterAPI{
  override def getRoute: MQRoute = ToCarInfoServersMessage.carInfoServersRoute
}

abstract class MSAkkaCarInfoServersMessageExtended[Ret:TypeTag]() extends AkkaCarInfoServersMessage{
  override type ReturnType = Ret
  override def getReturnTypeTag: TypeTag[Ret] = typeTag[Ret]
}
