package Plugins.CommonUtils.Utils

import java.util.regex.Pattern
import scala.collection.mutable.ListBuffer
import scala.util.Try

object RegexUtils {

  val chinesePattern: String = "[\\u4E00-\\u9FA5]+"

  /**
   * 匹配 JSON 字符串中指定 key 对应的 value 值,
   * - 如果有多个 key 只匹配第一个
   * - 只能匹配 value 为字符串, 即 `""` 包裹的内容 - regex = `\"key\": \"(.*?)\"`
   *
   * 注: 加了 `?` 为非贪心模式匹配
   */
  def getValueReplaceJson(jsonStr: String, keyName: String, newValue: String): String = {
    val regexOriginJson = "\\\"" + keyName + "\\\":\\\"(.*?)\\\""
    val newValOrigin = "\\\"" + keyName + "\\\":\\\"" + newValue + "\\\""

    val regexParseJson = "\\\\\"" + keyName + "\\\\\":\\\\\"(.*?)\\\\\""
    val newValParse = "\\\\\"" + keyName + "\\\\\":\\\\\""+ newValue +"\\\\\""

    jsonStr.replaceAll(regexOriginJson, newValOrigin).replaceAll(regexParseJson, newValParse)
  }


  def getValueOfJson(jsonStr: String, keyName: String): String = {
    val regexOriginJson = "\\\"" + keyName + "\\\":\\\"(.*?)\\\""
    val oldValOrigin = getMatchVal(regexOriginJson, jsonStr)
    if (oldValOrigin != "") {
      return oldValOrigin
    }

    val regexParseJson = "\\\\\"" + keyName + "\\\\\":\\\\\"(.*?)\\\\\""
    val oldValParse = getMatchVal(regexParseJson, jsonStr)
    if (oldValParse != ""){
      return oldValParse
    }

    ""
  }

  private def getMatchVal(regex: String, jsonStr: String): String = {
    val pattern = Pattern.compile(regex);
    val matcher = pattern.matcher(jsonStr)
    if (matcher.find()) {
      matcher.group(1)
    } else {
      ""
    }
  }


  /**
    * 匹配指定正则表达式的所有子串
    * @param input
    * @param regex
    * @return 如 input = `哈喽,ABC,你好` => return=哈喽你好`
    */
  def extractSubString(input: String, regex: String): String = {
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(input)

    val res = ListBuffer[String]()
    while (matcher.find){
      res.append(matcher.group)
    }

    res.foldLeft("")((pre, cur) => pre + cur)
  }


  def isIDNumber(IDNumber: String): String = { // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
    val regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"
    //假设18位身份证号码:41000119910101123X  410001 19910101 123X
    //^开头
    //[1-9] 第一位1-9中的一个      4
    //\\d{5} 五位数字           10001（前六位省市县地区）
    //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
    //\\d{2}                    91（年份）
    //((0[1-9])|(10|11|12))     01（月份）
    //(([0-2][1-9])|10|20|30|31)01（日期）
    //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
    //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
    //$结尾
    //假设15位身份证号码:410001910101123  410001 910101 123
    //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
    val matches = Try(IDNumber.matches(regularExpression))
    //判断第18位校验值
    if (matches.isSuccess && matches.get) {
      if (IDNumber.length == 18)
        try {
          val charArray = IDNumber.toCharArray
          //前十七位加权因子
          val idCardWi = Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
          //这是除以11后，可能产生的11位余数对应的验证码
          val idCardY = Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
          var sum = 0
          for (i <- idCardWi.indices) {
            val current = String.valueOf(charArray(i)).toInt
            val count = current * idCardWi(i)
            sum += count
          }
          val idCardLast = charArray(17)
          val idCardMod = sum % 11
          if (idCardY(idCardMod).toUpperCase == String.valueOf(idCardLast).toUpperCase) ""
          else "身份证最后一位:" + String.valueOf(idCardLast).toUpperCase + "错误,正确的应该是:" + idCardY(idCardMod).toUpperCase
        } catch {
          case e: Exception =>
            e.printStackTrace()
            "身份证号不正确：" + IDNumber
        }
      else ""
    } else
      s"出错了：身份证格式不正确 ${IDNumber}"
  }


  def isCellphone(cellphone: String): Boolean = {
    val reg = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))"
    cellphone.matches(reg)
  }
}
