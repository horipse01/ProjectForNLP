package Plugins.UserServersApi

import Plugins.UserServersApi.MSAkkaUserServersMessageExtended
case class ChangePasswordMessage(
  //学号
  studentId:String,
  oldPassword:String,
  newPassword:String,
) extends MSAkkaUserServersMessageExtended[String]
