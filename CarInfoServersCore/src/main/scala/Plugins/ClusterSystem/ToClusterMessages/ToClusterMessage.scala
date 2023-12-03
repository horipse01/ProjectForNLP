package Plugins.ClusterSystem.ToClusterMessages

import Plugins.CommonUtils.Types.JacksonSerializable
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[ToNewTestServiceMessage], name = "ToNewTestServiceMessage"),
    new JsonSubTypes.Type(value = classOf[ToFoodOrderMessage], name = "ToFoodOrderMessage"),
    new JsonSubTypes.Type(value = classOf[ToWShop2Message], name = "ToWShop2Message"),
    new JsonSubTypes.Type(value = classOf[ToMSFoodInfoMessage], name = "ToMSFoodInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToLogin2Message], name = "ToLogin2Message"),
    new JsonSubTypes.Type(value = classOf[ToApartmentInfoMessage], name = "ToApartmentInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToWshopInfoMessage], name = "ToWshopInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToNewTestService03Message], name = "ToNewTestService03Message"),
    new JsonSubTypes.Type(value = classOf[ToTestTimeout02Message], name = "ToTestTimeout02Message"),
    new JsonSubTypes.Type(value = classOf[ToHHLoginMessage], name = "ToHHLoginMessage"),
    new JsonSubTypes.Type(value = classOf[ToUsersInfoMessage], name = "ToUsersInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToH3InfoMessage], name = "ToH3InfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToUserServerMessage], name = "ToUserServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToHHShop2PortalMessage], name = "ToHHShop2PortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToUserOrderInfoMessage], name = "ToUserOrderInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToTimeOutTestMessage], name = "ToTimeOutTestMessage"),
    new JsonSubTypes.Type(value = classOf[ToOrderServersMessage], name = "ToOrderServersMessage"),
    new JsonSubTypes.Type(value = classOf[ToH4InfoMessage], name = "ToH4InfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToTimeoutTest04Message], name = "ToTimeoutTest04Message"),
    new JsonSubTypes.Type(value = classOf[ToFuritInfoMessage], name = "ToFuritInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToFuritServerMessage], name = "ToFuritServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToFoodInfoMessage], name = "ToFoodInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToFruitServerMessage], name = "ToFruitServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToCarServerMessage], name = "ToCarServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToUserAccountMessage], name = "ToUserAccountMessage"),
    new JsonSubTypes.Type(value = classOf[ToOrderServerMessage], name = "ToOrderServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToRentCarMallPortalMessage], name = "ToRentCarMallPortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToHHUserInfoMessage], name = "ToHHUserInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToTestServiceMessage], name = "ToTestServiceMessage"),
    new JsonSubTypes.Type(value = classOf[ToHHShop3PortalMessage], name = "ToHHShop3PortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToBooksInfoMessage], name = "ToBooksInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToUserInfoServerMessage], name = "ToUserInfoServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToMShopPortalMessage], name = "ToMShopPortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToHHShopPortalMessage], name = "ToHHShopPortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToWShop3Message], name = "ToWShop3Message"),
    new JsonSubTypes.Type(value = classOf[ToMSShopInfoMessage], name = "ToMSShopInfoMessage"),
    new JsonSubTypes.Type(value = classOf[ToUserServersMessage], name = "ToUserServersMessage"),
    new JsonSubTypes.Type(value = classOf[ToNewTestService02Message], name = "ToNewTestService02Message"),
    new JsonSubTypes.Type(value = classOf[ToCarInfoServersMessage], name = "ToCarInfoServersMessage"),
    new JsonSubTypes.Type(value = classOf[ToFoodInfoServerMessage], name = "ToFoodInfoServerMessage"),
    new JsonSubTypes.Type(value = classOf[ToRentCarShopPortalMessage], name = "ToRentCarShopPortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToWShopPortalMessage], name = "ToWShopPortalMessage"),
    new JsonSubTypes.Type(value = classOf[ToTestService04Message], name = "ToTestService04Message"),
  )
)
abstract class ToClusterMessage(val serializedInfo : String) extends JacksonSerializable{
  def route:MQRoute
}
