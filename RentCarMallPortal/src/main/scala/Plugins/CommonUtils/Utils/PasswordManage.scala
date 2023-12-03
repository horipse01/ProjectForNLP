package Plugins.CommonUtils.Utils

import java.security.SecureRandom
import java.util
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/** 密码管理 */
object PasswordManage {
  private val ITERATIONS = 100
  private val KEY_LENGTH = 512
  private val ALGORITHM = "PBKDF2WithHmacSHA512"

  def generateSalt(): String = {
    val RAND: SecureRandom = new SecureRandom()
    val salt: Array[Byte] = new Array[Byte](KEY_LENGTH)
    RAND.nextBytes(salt)
    Base64.getEncoder.encodeToString(salt)
  }

  def hashPassword(chars: Array[Char], salt: String): String = {
    val salt_bytes: Array[Byte] = salt.getBytes()
    val spec = new PBEKeySpec(chars, salt_bytes, ITERATIONS, KEY_LENGTH)
    util.Arrays.fill(chars, Character.MIN_VALUE)
    try {
      val fac = SecretKeyFactory.getInstance(ALGORITHM)
      val securePassword = fac.generateSecret(spec).getEncoded
      Base64.getEncoder.encodeToString(securePassword)
    } catch {
      case e: Exception =>
        MailSender.emailException(e)
        "Error"
    } finally spec.clearPassword()
  }

  def verifyPassword(password: Array[Char], salt: String, key: String): Boolean = {
    val optEncrypted = hashPassword(password, salt)
    optEncrypted == key
  }
}
