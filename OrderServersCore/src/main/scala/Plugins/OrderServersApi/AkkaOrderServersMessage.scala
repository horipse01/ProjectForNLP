package Plugins.OrderServersApi

import Plugins.ClusterSystem.Extension.ClusterAPI
import Plugins.ClusterSystem.ToClusterMessages.{MQRoute, ToOrderServersMessage}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import scala.reflect.runtime.universe._

import Plugins.OrderServersApi.RentCarOrderMessage
import Plugins.OrderServersApi.GetOrderListMessage

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
Array(
  new JsonSubTypes.Type(value = classOf[RentCarOrderMessage], name = "RentCarOrderMessage"),
  new JsonSubTypes.Type(value = classOf[GetOrderListMessage], name = "GetOrderListMessage"),
))
abstract class AkkaOrderServersMessage() extends ClusterAPI{
  override def getRoute: MQRoute = ToOrderServersMessage.orderServersRoute
}

abstract class MSAkkaOrderServersMessageExtended[Ret:TypeTag]() extends AkkaOrderServersMessage{
  override type ReturnType = Ret
  override def getReturnTypeTag: TypeTag[Ret] = typeTag[Ret]
}
