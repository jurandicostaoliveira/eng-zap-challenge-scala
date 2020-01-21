package br.com.btg360.services

import br.com.btg360.application.Service
import org.json4s._
import org.json4s.jackson.Serialization.{read, write}


class JsonService extends Service {

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
