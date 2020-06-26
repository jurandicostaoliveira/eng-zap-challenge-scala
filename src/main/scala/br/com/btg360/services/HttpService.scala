package br.com.btg360.services

import br.com.btg360.application.Service
import com.m3.curly.{HTTP, Request, Response}

class HttpService extends Service {

  private var _url: String = _

  private var _headers: Map[String, String] = Map()

  /**
    * Getter
    *
    * @return String
    */
  def url: String = this._url

  /**
    * Setter
    *
    * @param String value
    * @return this
    */
  def url(value: String): HttpService = {
    this._url = value
    this
  }

  /**
    * Getter
    *
    * @return Map
    */
  def headers: Map[String, String] = this._headers

  /**
    * Setter
    *
    * @param Map value
    * @return this
    */
  def headers(value: Map[String, String]): HttpService = {
    this._headers = value
    this
  }

  /**
    * @return Request
    */
  private def request: Request = {
    val request: Request = new Request(this.url).setHeader("cache-control", "no-cache")
    for ((key, value) <- this.headers) {
      request.setHeader(key, value)
    }

    request
  }

  /**
    * Get method
    *
    * @return Response
    */
  def get: Response = {
    try {
      HTTP.get(this.request)
    } catch {
      case e: Exception => println(e)
        new Response()
    }
  }

  /**
    * Get raw string
    *
    * @return Response
    */
  def getRaw: String = {
    this.get.getTextBody
  }

  /**
    * Post method
    */
  def post: Unit = {
    //Todo
  }

  /**
    * Put method
    */
  def put: Unit = {
    //Todo
  }

  /**
    * Delete method
    */
  def delete: Unit = {
    //Todo
  }

}
