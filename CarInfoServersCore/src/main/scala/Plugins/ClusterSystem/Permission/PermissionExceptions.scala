package Plugins.ClusterSystem.Permission

import Plugins.CommonUtils.Exceptions.ExceptionWithMessage


case class MessageTypeAlreadyExistsException() extends ExceptionWithMessage {
  override val message: String = "错误：该消息类型已经存在！"
}

case class MessageTypeNotExistException() extends ExceptionWithMessage {
  override val message: String = "错误：不存在的消息类型！"
}
case class PermissionDeniedException() extends ExceptionWithMessage {
  override val message: String = "错误：权限不足！"
}