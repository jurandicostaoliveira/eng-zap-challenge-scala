package br.com.btg360.constants

object Channel {

  val EMAIL_ID = 1
  val EMAIL = "email"

  val SMS_ID = 2
  val SMS = "sms"

  val MOBILE_ID = 3
  val MOBILE = "mobile"

  val FACEBOOK_ID = 4
  val FACEBOOK = "facebook"

  val WEBPUSH_ID = 5
  val WEBPUSH = "webpush"

  val PUSH_ANDROID_ID = 6
  val PUSH_ANDROID = "push_android"

  val PUSH_IOS_ID = 7
  val PUSH_IOS = "push_ios"

  /**
    * Verify that the channel type is an email or not
    *
    * @return
    */
  def isEmailChannel(channelName: String): Boolean = {
    channelName == this.EMAIL
  }

}
