package Plugins.CommonUtils.TypedSystem.API

import Plugins.CommonUtils.Types.JacksonSerializable

import scala.util.matching.Regex

case class PlanUUID(id:String) extends JacksonSerializable {
  def addTail(tail: String): PlanUUID =
    PlanUUID(id + "-" + tail)

  override def toString: String = id
}

object PlanUUID {

  val UUIDRegex1: Regex = """.*?"uuid":\{"id":"([0-9a-zA-Z-_]+)"}.*?""".r
  val UUIDRegex2: Regex = """.*?\\"uuid\\":\{\\"id\\":\\"([0-9a-zA-Z-_]+)\\"}.*?""".r
  val UUIDRegex3: Regex = """.*?\\\\\\"uuid\\\\\\":\{\\\\\\"id\\\\\\":\\\\\\"([0-9a-zA-Z-_]+)\\\\\\"}.*?""".r

  def getUUID(jsonStr: String): PlanUUID = {
    jsonStr match {
      case UUIDRegex1(id) => PlanUUID(id)
      case UUIDRegex2(id) => PlanUUID(id)
      case UUIDRegex3(id) => PlanUUID(id)
      case _ => API.initUUID
    }
  }
}