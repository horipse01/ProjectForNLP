package Plugins.UserServersApi

import Plugins.UserServersApi.MSAkkaUserServersMessageExtended
case class QuestionMessage(
                              userToken:String,
                              question:String,
                            ) extends MSAkkaUserServersMessageExtended[String]
