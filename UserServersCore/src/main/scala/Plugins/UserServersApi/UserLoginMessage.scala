package Plugins.UserServersApi

import Plugins.UserServersApi.MSAkkaUserServersMessageExtended


case class UserLoginMessage(
  //改成学号userName:String,
  studentId:String,
  password:String,
) extends MSAkkaUserServersMessageExtended[String]
