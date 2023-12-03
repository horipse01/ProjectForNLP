package Plugins.CommonUtils.TypedSystem.API

import Plugins.CommonUtils.Types.JacksonSerializable
import Plugins.CommonUtils.Utils.StringUtils
import com.fasterxml.jackson.annotation.JsonIgnore
import com.typesafe.scalalogging.Logger
import org.joda.time.DateTime

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

/** API的基本类型，保存了API返回的数据类型 ReturnType */
abstract class API() extends JacksonSerializable {
  type ReturnType
  var uuid:PlanUUID=PlanUUID("")

  def copyUUID(newUUID: PlanUUID): this.type = {
    uuid = newUUID
    this
  }

  @JsonIgnore
  def getReturnTypeTag:TypeTag[ReturnType]
  @JsonIgnore
  def getReturnClassTag:ClassTag[ReturnType]= {
    val tag=getReturnTypeTag
    ClassTag[ReturnType](tag.mirror.runtimeClass(tag.tpe))
  }

  def withUUID(uuid:String):this.type={
    this.uuid= PlanUUID(uuid)
    this
  }

  /** 表示当前API是否会产生回复。如果不会产生回复，则这里会复写成false */
  def hasReply:Boolean= API.hasReply(getReturnClassTag.toString())

  @deprecated(message = "use [[Plugins.CommonUtils.Utils.IOUtils.logger]]", since = "后端 planner 内部的其它层级的方法不方便打印日志")
  def logger: Logger = Logger(this.getClass.getSimpleName + "-" +uuid.id)
}

object API{
  /** 初始化uuid */
  def initUUID: PlanUUID= PlanUUID(DateTime.now().getMillis.toString + "_" + StringUtils.randomLetterString(10))

  def hasReply(className:String):Boolean= {
    className!="Unit" && className!="Nothing"
  }
}