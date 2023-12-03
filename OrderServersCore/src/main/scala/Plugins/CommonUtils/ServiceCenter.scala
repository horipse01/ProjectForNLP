
package Plugins.CommonUtils

import Globals.GlobalVariables.serviceCode


/** 保存所有的服务注册信息 */
object ServiceCenter {

  val newTestServiceServiceCode = "A000028"
  val foodOrderServiceCode = "A000005"
  val wShop2ServiceCode = "A000026"
  val mSFoodInfoServiceCode = "A000015"
  val login2ServiceCode = "A000019"
  val apartmentInfoServiceCode = "A000008"
  val wshopInfoServiceCode = "A000011"
  val newTestService03ServiceCode = "A000031"
  val testTimeout02ServiceCode = "A000034"
  val hHLoginServiceCode = "A000017"
  val usersInfoServiceCode = "A000009"
  val h3InfoServiceCode = "A000022"
  val userServerServiceCode = "A000001"
  val hHShop2PortalServiceCode = "A000018"
  val userOrderInfoServiceCode = "A000010"
  val timeOutTestServiceCode = "A000033"
  val orderServersServiceCode = "A000043"
  val h4InfoServiceCode = "A000039"
  val timeoutTest04ServiceCode = "A000035"
  val buyACarPortalServiceCode = "A000000"
  val furitInfoServiceCode = "A000006"
  val furitServerServiceCode = "A000037"
  val foodInfoServiceCode = "A000004"
  val fruitServerServiceCode = "A000036"
  val carServerServiceCode = "A000025"
  val userAccountServiceCode = "A000003"
  val orderServerServiceCode = "A000029"
  val rentCarMallPortalServiceCode = "A000040"
  val hHUserInfoServiceCode = "A000021"
  val testServiceServiceCode = "A000012"
  val hHShop3PortalServiceCode = "A000020"
  val booksInfoServiceCode = "A000013"
  val userInfoServerServiceCode = "A000024"
  val mShopPortalServiceCode = "A000002"
  val hHShopPortalServiceCode = "A000016"
  val wShop3ServiceCode = "A000027"
  val mSShopInfoServiceCode = "A000014"
  val userServersServiceCode = "A000041"
  val newTestService02ServiceCode = "A000030"
  val carInfoServersServiceCode = "A000042"
  val foodInfoServerServiceCode = "A000038"
  val rentCarShopPortalServiceCode = "A000023"
  val wShopPortalServiceCode = "A000007"
  val testService04ServiceCode = "A000032"

  /** ***************************************************************** */

  val fullNameMap: Map[String, String] = Map(
    newTestServiceServiceCode -> "NewTestService",
    foodOrderServiceCode -> "FoodOrder",
    wShop2ServiceCode -> "WShop2",
    mSFoodInfoServiceCode -> "MSFoodInfo",
    login2ServiceCode -> "Login2",
    apartmentInfoServiceCode -> "ApartmentInfo",
    wshopInfoServiceCode -> "WshopInfo",
    newTestService03ServiceCode -> "NewTestService03",
    testTimeout02ServiceCode -> "TestTimeout02",
    hHLoginServiceCode -> "HHLogin",
    usersInfoServiceCode -> "UsersInfo",
    h3InfoServiceCode -> "H3Info",
    userServerServiceCode -> "UserServer",
    hHShop2PortalServiceCode -> "HHShop2Portal",
    userOrderInfoServiceCode -> "UserOrderInfo",
    timeOutTestServiceCode -> "TimeOutTest",
    orderServersServiceCode -> "OrderServers",
    h4InfoServiceCode -> "H4Info",
    timeoutTest04ServiceCode -> "TimeoutTest04",
    buyACarPortalServiceCode -> "BuyACarPortal",
    furitInfoServiceCode -> "FuritInfo",
    furitServerServiceCode -> "FuritServer",
    foodInfoServiceCode -> "FoodInfo",
    fruitServerServiceCode -> "FruitServer",
    carServerServiceCode -> "CarServer",
    userAccountServiceCode -> "UserAccount",
    orderServerServiceCode -> "OrderServer",
    rentCarMallPortalServiceCode -> "RentCarMallPortal",
    hHUserInfoServiceCode -> "HHUserInfo",
    testServiceServiceCode -> "TestService",
    hHShop3PortalServiceCode -> "HHShop3Portal",
    booksInfoServiceCode -> "BooksInfo",
    userInfoServerServiceCode -> "UserInfoServer",
    mShopPortalServiceCode -> "MShopPortal",
    hHShopPortalServiceCode -> "HHShopPortal",
    wShop3ServiceCode -> "WShop3",
    mSShopInfoServiceCode -> "MSShopInfo",
    userServersServiceCode -> "UserServers",
    newTestService02ServiceCode -> "NewTestService02",
    carInfoServersServiceCode -> "CarInfoServers",
    foodInfoServerServiceCode -> "FoodInfoServer",
    rentCarShopPortalServiceCode -> "RentCarShopPortal",
    wShopPortalServiceCode -> "WShopPortal",
    testService04ServiceCode -> "TestService04",
  )

  val portalPortMap: Map[String, Int] = Map(
    hHShop2PortalServiceCode -> 6000,
    buyACarPortalServiceCode -> 6001,
    rentCarMallPortalServiceCode -> 6002,
    hHShop3PortalServiceCode -> 6003,
    mShopPortalServiceCode -> 6004,
    hHShopPortalServiceCode -> 6005,
    rentCarShopPortalServiceCode -> 6006,
    wShopPortalServiceCode -> 6007,
  )

  def portMap(serviceCode: String): Int = {
    serviceCode.drop(1).toInt +
      (if (serviceCode.head == 'A') 10000 else if (serviceCode.head == 'D') 20000 else 30000)
  }

  def serviceName(serviceCode: String): String = {
    fullNameMap(serviceCode).toLowerCase
  }

  def serverHostName: String = "localhost"

  def dbServerName: String = "postgres-service"

  def serverPort(serviceCode: String): String = serverHostName + ":" + portMap(serviceCode)

  def mainSchema: Option[String] = Some(serviceName(serviceCode).replace("-", "_"))

  val seedNodeName: String = List(serviceCode).map(
    "\"akka://QianFangCluster@" + serverPort(_) + "\""
  ).reduce(_ + "," + _)

  lazy val servicePort: Int = portMap(serviceCode)
  lazy val serviceFullName: String = fullNameMap(serviceCode)
}

