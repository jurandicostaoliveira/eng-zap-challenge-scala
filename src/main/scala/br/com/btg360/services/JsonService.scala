package br.com.btg360.services

import org.json4s._
import org.json4s.native.Serialization.read
import org.json4s.native.Serialization.write


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
    read[T](jsonString)
  }

  /**
    * Json encoder
    *
    * To encode it is necessary to be a case class
    *
    * @param AnyRef caseClass
    * @return String
    */
  def encode(caseClass: AnyRef): String = {
    write(caseClass)
  }

}
