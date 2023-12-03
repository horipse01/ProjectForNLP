package Plugins.CommonUtils.Utils

import Plugins.CommonUtils.Exceptions.{ExceptionWithMessage, IllegalArgumentException}

object ParamUtils {

  def require(requirement: Boolean, errMsg: String = ""): Unit = {
    if (!requirement)
      throw IllegalArgumentException(errMsg)
  }

  def require(requirement: Boolean, ex: ExceptionWithMessage): Unit = {
    if (!requirement)
      throw ex
  }

  def notEmpty(str: String): Boolean = {
    str != null && !str.equals("")
  }

  def notEmpty[T](list: List[T]): Boolean = {
    list != null && list.nonEmpty
  }

  def notNull(obj: AnyRef): Boolean = {
    obj != null
  }

}
