package br.com.btg360.services

import java.util.Base64

object Base64Service {

  val US_ASCII: String = "US-ASCII"
  val ISO_8859_1: String = "ISO-8859-1"
  val UTF_8: String = "UTF-8"
  val UTF_16BE: String = "UTF-16BE"
  val UTF_16LE: String = "UTF-16LE"
  val UTF_16: String = "UTF-16"

  /**
    * @param String value
    * @param String charset
    * @return String
    */
  def encode(value: String, charset: String = Base64Service.UTF_8): String = {
    Base64.getEncoder.encodeToString(value.getBytes(charset))
  }

  /**
    * @param String value
    * @param String charset
    * @return
    */
  def decode(value: String, charset: String = Base64Service.UTF_8): String = {
    new String(Base64.getDecoder.decode(value), charset)
  }

}
