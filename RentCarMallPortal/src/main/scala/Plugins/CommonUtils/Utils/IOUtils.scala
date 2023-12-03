package Plugins.CommonUtils.Utils

import Globals.GlobalVariables
import Plugins.CommonUtils.Exceptions.{CodeException, DeleteFileException, ExceptionWithCode, ExceptionWithMessage, MessageException, UnknownReplyMessageTypeException}
import Plugins.CommonUtils.TypedSystem.API.PlanUUID
import Plugins.CommonUtils.Types.{CollectionsTypeFactory, ReplyMessage}
import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.typesafe.scalalogging.Logger
import org.apache.commons.codec.binary.Base64

import java.io.{BufferedReader, ByteArrayInputStream, ByteArrayOutputStream, File, InputStreamReader, ObjectOutputStream}
import java.util
import java.util.zip.{GZIPInputStream, GZIPOutputStream}
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{TypeTag, typeOf}
import scala.util.{Failure, Success, Try}

object IOUtils {
  /**
   * Jackson使用的 object mapper
   *
   * 当前配置的情况为:
   * - Class 所需要的字段 json字符串中 必须有,
   * - Class 不需要的字段 json字符串中有的 会被忽略掉
   *
   * FAIL_ON_UNKNOWN_PROPERTIES = false       => 忽略未知字段
   * FAIL_ON_NULL_CREATOR_PROPERTIES = true   => 校验 AnyRef类型的字段不能为 null, AnyValue(Int, Long, Double等) 会初始化为默认值(0, 0L, 0.0 等)
   */
  val objectMapper: ObjectMapper = new ObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    // .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true)
    .registerModule(DefaultScalaModule)

  /** 序列化 */
  def serialize(o: Any): String =
    o match {
      case Some(o2) =>
        serialize(o2)
      case o2: List[_] =>
        o2.map(IOUtils.serialize).mkString("[", ",", "]")
      case _ =>
        objectMapper.writeValueAsString(o)
    }

  /** 把返回的消息变成一个reply message */
  def serializeAsReply(o: Any): ReplyMessage = ReplyMessage(0, serialize(o))


  def generateClassTag[T](className: String): ClassTag[T] = {
    className match {
      case "Byte" => ClassTag(java.lang.Byte.TYPE)
      case "Short" => ClassTag(java.lang.Short.TYPE)
      case "Char" => ClassTag(java.lang.Character.TYPE)
      case "Int" => ClassTag(java.lang.Integer.TYPE)
      case "Long" => ClassTag(java.lang.Long.TYPE)
      case "Float" => ClassTag(java.lang.Float.TYPE)
      case "Double" => ClassTag(java.lang.Double.TYPE)
      case "Boolean" => ClassTag(java.lang.Boolean.TYPE)
      case "Void" | "Unit" => ClassTag(java.lang.Void.TYPE)
      case "String" | "Nothing" | "Null" => ClassTag.Object.asInstanceOf[ClassTag[T]]
      case _ => ClassTag(Class.forName(className))
    }
  }

  def deserialize[T: TypeTag](bytes: String)(implicit tag: ClassTag[T]): T = {
    val fullType: String = typeOf[T].toString

    if (fullType.startsWith("Map["))
      return objectMapper.readValue(bytes.getBytes(), tag.runtimeClass).asInstanceOf[T]
    fullType.count(_ == '[') match {
      case 0 =>
        objectMapper.readValue(bytes.getBytes(), tag.runtimeClass).asInstanceOf[T]
      case 1 =>
        val subType: ClassTag[T] = generateClassTag[T](fullType.substring(fullType.indexOf('[') + 1, fullType.length - 1))
        tag.toString() match {
          case "scala.collection.immutable.List" | "scala.collection.Seq" =>
            deserializeList(bytes)(subType).asInstanceOf[T]
          case "scala.Option" =>
            deserializeOption(bytes)(subType).asInstanceOf[T]
        }
      case _ =>
        val className = fullType.substring(fullType.lastIndexOf('[') + 1, fullType.indexOf(']'))
        val subType: ClassTag[T] = generateClassTag[T](className)
        if (fullType.startsWith("List[List["))
          deserializeListList(bytes)(subType).asInstanceOf[T]
        else if (fullType.startsWith("List[Option["))
          deserializeListOption(bytes)(subType).asInstanceOf[T]
        else if (fullType.startsWith("Option[List["))
          deserializeOptionList(bytes)(subType).get.asInstanceOf[T]
        else if (fullType.startsWith("Option[Option[")) {
          deserializeOptionOption(bytes)(subType).get.asInstanceOf[T]
        } else
          objectMapper.readValue(bytes.getBytes(), tag.runtimeClass).asInstanceOf[T]
    }
  }

  def deserializeList[T](bytes: String)(implicit tag: ClassTag[T]): List[T] = {
    objectMapper.readValue(bytes.getBytes(), CollectionsTypeFactory.listOf(tag.runtimeClass)).asInstanceOf[util.ArrayList[T]].
      toArray().asInstanceOf[Array[T]].toList
  }

  def deserializeOption[T](bytes: String)(implicit tag: ClassTag[T]): Option[T] = {
    if (bytes == "null") None
    else Some(objectMapper.readValue(bytes.getBytes(), tag.runtimeClass).asInstanceOf[T])
  }

  def deserializeListList[T](bytes: String)(implicit tag: ClassTag[T]): List[List[T]] = {
    objectMapper.readValue(bytes.getBytes(), CollectionsTypeFactory.listOfListOf(tag.runtimeClass)).
      asInstanceOf[util.ArrayList[util.ArrayList[T]]].toArray().map(_.asInstanceOf[util.ArrayList[T]].toArray().asInstanceOf[Array[T]].toList).toList
  }

  def deserializeListOption[T](bytes: String)(implicit tag: ClassTag[T]): List[Option[T]] = {
    objectMapper.readValue(bytes.getBytes(), CollectionsTypeFactory.listOf(tag.runtimeClass)).asInstanceOf[util.ArrayList[AnyRef]].
      toArray().map { r => if (r == null) None else Some(r.asInstanceOf[T]) }.toList
  }

  def deserializeOptionList[T](bytes: String)(implicit tag: ClassTag[T]): Option[List[T]] = {
    if (bytes == "null") None
    else Some(deserializeList(bytes)(tag))
  }

  def deserializeOptionOption[T](bytes: String)(implicit tag: ClassTag[T]): Option[Option[T]] = {
    if (bytes == "null") None
    else Some(Some(objectMapper.readValue(bytes.getBytes(), tag.runtimeClass).asInstanceOf[T]))
  }


  def fromObject(success: Boolean, reply: Object): HttpResponse = HttpResponse(
    status = {
      if (success) StatusCodes.OK else StatusCodes.BadRequest
    },
    entity = IOUtils.serialize(reply)
  )

  def fromString(success: Boolean, reply: String): HttpResponse = HttpResponse(
    status = {
      if (success) StatusCodes.OK else StatusCodes.BadRequest
    },
    entity = reply
  ).addHeader(RawHeader("Access-Control-Allow-Origin", "*"))


  private val passwordKey = "password"

  def passwordRemoval(map: Map[String, Any]): Map[String, Any] = {
    var obj: Map[String, Any] = map
    val specialKeys = List("serializedInfo", "toClusterMessage")
    if (obj.contains(passwordKey))
      obj = obj.updated(passwordKey, "")
    specialKeys.foreach(key =>
      if (obj.contains(key)) {
        obj = obj.updated(key,
          obj(key) match {
            case a: String => passwordRemoval(a)
            case a: Any => passwordRemoval(a.asInstanceOf[Map[String, Any]])
          }
        )
      }
    )
    obj
  }

  def passwordRemoval(bytes: String): String = {
    if (bytes.contains(passwordKey)) {
      IOUtils.serialize(passwordRemoval(IOUtils.deserialize[Map[String, Any]](bytes)))
    } else {
      bytes
    }
  }

  /** 把exception变成字符串 */
  def exceptionToString(e: Throwable): String =
    e.getClass.getName + ":" +
      e.getMessage + "\n" +
      e.getStackTrace.map(_.toString).fold("")(_ + "\n" + _)

  /** 把exception变成replymessage */
  def exceptionToReply(e: Throwable, uuid: String = ""): String =
    IOUtils.serialize(ReplyMessage(-1, exceptionToString(e), uuid))

  /** 把exception变成reply message, status=2, info=code。这个方法专门用于处理运行过程中没有编码的exception，所以编码为0000，
   * 并且附上了错误的具体内容 */
  def exceptionToReplyCode(e: Throwable, uuid: String): ReplyMessage = {
    ReplyMessage(-2, GlobalVariables.serviceCode + "0000" + exceptionToString(e), uuid)
  }

  def resultToReply[T](result: Try[T], uuid: PlanUUID)(implicit typeTag: TypeTag[T], classTag: ClassTag[T]): ReplyMessage = {
    result match {
      case Success(value) =>
        if (classTag.toString == "java.lang.String")
          ReplyMessage(0, value.asInstanceOf[String], uuid.id)
        else
          ReplyMessage(0, IOUtils.serialize(value), uuid.id)
      case Failure(messageException: ExceptionWithMessage) =>
        IOUtils.logger(uuid).error(s"捕捉到已知异常：${messageException.getMessage}", messageException)
        ReplyMessage(-1, messageException.getMessage, uuid.id)
      case Failure(codeException: ExceptionWithCode) =>
        IOUtils.logger(uuid).error(s"捕捉到严重异常：${codeException.getMessage}", codeException)
        ReplyMessage(-2, codeException.getMessage, uuid.id)
      case Failure(exception) =>
        IOUtils.logger(uuid).error(s"出现未知错误：${exception.getMessage}", exception)
        exceptionToReplyCode(exception, uuid.id)
    }
  }

  def replyToResult[T](replyMessage: ReplyMessage)(implicit typeTag: TypeTag[T], classTag: ClassTag[T]): Try[T] = Try {
    replyMessage.status match {
      case 0 =>
        if (classTag.toString == "java.lang.String")
          replyMessage.info.asInstanceOf[T]
        else IOUtils.deserialize[T](replyMessage.info)
      case -1 => throw MessageException(replyMessage.info)
      case -2 => throw CodeException(replyMessage.info)
      case _ => throw UnknownReplyMessageTypeException()
    }
  }

  /** 看看folder是否存在 */
  def checkFolder(folderName: String): Unit = {
    val path = new File(folderName)
    if (!path.exists()) path.mkdirs()
  }

  /** 强制删除文件 */
  def deleteFile(fileName: String): Boolean = {
    val file = new File(fileName)
    if (file.exists()) {
      var deleteResult: Boolean = false
      var tryCount = 0
      while (!deleteResult && tryCount < 10) {
        System.gc()
        deleteResult = file.delete()
        tryCount += 1
      }
      if (deleteResult)
        return true
      else throw DeleteFileException()
    }
    false
  }

  def logger(implicit uuid: PlanUUID): Logger = Logger(uuid.id)

  def printInfo(message: String)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).info(message)

  def printInfo(message: String, e: Throwable)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).info(message, e)

  def printInfo(message: String, args: Any*)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).info(message, args.map(smartToString): _*)

  def printError(message: String)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).error(message)

  def printError(message: String, e: Throwable)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).error(message, e)

  def printError(message: String, args: Any*)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).error(message, args.map(smartToString): _*)

  def printWarning(message: String)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).warn(message)

  def printWarning(message: String, e: Throwable)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).warn(message, e)

  def printWarning(message: String, args: Any*)(implicit uuid: PlanUUID): Unit = Logger(uuid.id).warn(message, args.map(smartToString): _*)

  /** 压缩String成字节流 */
  def compressString(inputString: String): Array[Byte] = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)
    try {
      gzipOutputStream.write(inputString.getBytes())
    } finally {
      gzipOutputStream.close()
    }
    byteArrayOutputStream.toByteArray
  }

  /** 解压字节流为String */
  def decompressGzip(gzipData: Array[Byte]): String = {
    val gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(gzipData))
    val reader = new BufferedReader(new InputStreamReader(gzipInputStream))
    val plainData = Iterator.continually(reader.readLine()).takeWhile(_ != null).mkString("\n")
    reader.close()
    plainData
  }


  /** 先将对象序列化为字符串，然后压缩为字节流，并转换为base64 */
  def compressObjectToBase64(obj: Any): String = {
    Base64.encodeBase64String(compressString(IOUtils.serialize(obj)))
  }

  /** 将base64转换为字节流，并解压缩为String，然后反序列化为对象 */
  def decompressGzipBase64[T: TypeTag](gzipData: String)(implicit tag: ClassTag[T]): T = {
    val binaryData = Base64.decodeBase64(gzipData)
    IOUtils.deserialize[T](decompressGzip(binaryData))
  }

  /** 计算对象占用内存空间 */
  def calculatorSize(obj: Object): Int = {
    val byteArrayOutputStream = new ByteArrayOutputStream()
    val objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
    objectOutputStream.writeObject(obj)
    objectOutputStream.flush()
    byteArrayOutputStream.size()
  }

  /**
   * 避免原架构使用 null.toString 输出 [[NullPointerException]]
   */
  private def smartToString(obj: Any): String = {
    if (obj == null) {
      "null"
    }
    else {
      obj.toString
    }
  }

}
