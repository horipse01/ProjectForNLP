package Plugins.UserServersShared

import Plugins.CommonUtils.Types.JacksonSerializable

case class HistoryInformation(
                             //用户id，学号，查询内容(String)，返回内容(String)，引用文件数(Int)，引用文件名（String），时间戳
                                id:Int,
                                username:String,
                                query:String,
                                result:String,
                                fileNum:Int,
                                fileName:String,
                                timestamp:String,
                          ) extends JacksonSerializable
