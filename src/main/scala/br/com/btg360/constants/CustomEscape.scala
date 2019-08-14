package br.com.btg360.constants

import org.apache.commons.lang3.StringEscapeUtils

object CustomEscape {

  /**
    * @param String value
    * @return String
    */
  def toJson(value: String): String = {
    StringEscapeUtils.escapeJson(value)
      .replace("'", "\\u0027")
  }

}
