package Plugins.CommonUtils.Exceptions

abstract class ExceptionWithMessage() extends Exception {
  val message:String
  override def getMessage: String = message
}

case class MessageException(override val message: String="用户操作执行错误！") extends ExceptionWithMessage

case class ConnectionFailedException() extends ExceptionWithMessage {
  override val message: String = "连接错误！"
}

case class HandleTimeoutException() extends ExceptionWithMessage {
  override val message: String = "错误：API处理超时！"
}

case class IllegalArgumentException(errMsg: String = "参数错误") extends ExceptionWithMessage {
  override val message: String = s"错误: ${errMsg}"
}
