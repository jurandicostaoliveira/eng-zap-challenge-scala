package br.com.btg360.constants

object Channel {

  val EMAIL_ID: Int = 1
  val EMAIL: String = "email"

  val SMS_ID: Int = 2
  val SMS: String = "sms"

  val MOBILE_ID: Int = 3
  val MOBILE: String = "mobile"

  val FACEBOOK_ID: Int = 4
  val FACEBOOK: String = "facebook"

  val WEBPUSH_ID: Int = 5
  val WEBPUSH: String = "webpush"

  val PUSH_ANDROID_ID: Int = 6
  val PUSH_ANDROID: String = "push_android"

  val PUSH_IOS_ID: Int = 7
  val PUSH_IOS: String = "push_ios"

  /**
    * Verify that the channel type is an email or not
    *
    * @return
    */
  def isEmail(channelName: String): Boolean = {
    channelName == this.EMAIL
  }

  /**
    *
    * @return
    */
  def all: Map[String, Int] = {
    Map(
      this.EMAIL -> this.EMAIL_ID,
      this.SMS -> this.SMS_ID,
      this.FACEBOOK -> this.FACEBOOK_ID,
      this.WEBPUSH -> this.WEBPUSH_ID,
      this.PUSH_ANDROID -> this.PUSH_ANDROID_ID,
      this.PUSH_IOS -> this.PUSH_IOS_ID
    )
  }

}
