package Plugins.UserServersApi

import Plugins.UserServersApi.MSAkkaUserServersMessageExtended


case class UseRegisterMessage(
  userName:String,
  email:String,
  password:String,
                             //加学号：
  studentId:String,
) extends MSAkkaUserServersMessageExtended[String]
