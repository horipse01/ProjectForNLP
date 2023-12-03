package Plugins.CommonUtils.Utils

import Plugins.CommonUtils.UserPath.chosenPath
import com.typesafe.config.{Config, ConfigFactory}
import slick.jdbc.PostgresProfile.api._

object DBUtils {
  lazy val DBConfig: Config = ConfigFactory
    .parseString(
      s"""
     th.properties.serverName=${chosenPath.dbServerName()},
     th.properties.portNumber=${chosenPath.dbPort().toString}
      """) withFallback (ConfigFactory.load())
  lazy val db = Database.forConfig("th", config = DBConfig)
}
