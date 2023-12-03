package Plugins.UserServersApi

import Plugins.UserServersApi.MSAkkaUserServersMessageExtended
case class GetHistoryMessage(
                                  studentId:String,
                                ) extends MSAkkaUserServersMessageExtended[String]

