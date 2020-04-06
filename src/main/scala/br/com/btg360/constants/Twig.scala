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

  /**
    * Replace url encode for twig tags
    *
    * @param String value
    * @return String
    */
  def toUrlEncode(value: String): String = {
    val patterns: Map[String, String] = Map(
      "%7B%7B" -> "{{", "%20" -> "", "%7D%7D" -> "}}"
    )

    var replacement: String = value
    for ((key, value) <- patterns) {
      replacement = replacement.replaceAll(key, value)
    }

    replacement
  }

}
