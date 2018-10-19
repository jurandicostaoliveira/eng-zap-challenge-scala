package br.com.btg360.services

import org.json4s._
import org.json4s.native.JsonMethods._

class JsonService() {

  implicit val formats = DefaultFormats

  /**
    * Json decoder
    *
    * @param String jsonString
    * @tparam T
    * @return T
    */
  def decode[T: Manifest](jsonString: String): T = {
    parse(jsonString).extract[T]
  }

  def encode[T: Manifest](value: T): String = {
    null
  }

}
