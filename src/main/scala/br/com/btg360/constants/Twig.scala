package br.com.btg360.constants

object Twig {

  /**
    * Check for a twig tag
    *
    * @param String value
    * @return Boolean
    */
  def contains(value: String): Boolean = {
    "(\\{\\{)(.*?)(\\}\\})".r.findFirstIn(value).nonEmpty
  }

}
