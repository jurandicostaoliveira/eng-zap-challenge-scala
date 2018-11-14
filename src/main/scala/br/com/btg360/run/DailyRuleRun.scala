package br.com.btg360.run

import br.com.btg360.worker.rule.Daily

import scala.util.matching.Regex

object DailyRuleRun extends App {

  new Daily().dispatch(14)


//  var html: String =
//    """
//      <html><a href = 'http://google.com.br'>Google</a>
//      \n
//      <a href=   \n   "http://yahoo.com.br">Yahoo</a></html>
//    """
//
//  val patterns: Map[String, String] = Map(
//    "(\\\\n)" -> "",
//    "href[\\s]+\\=[\\s]+|href[\\s]+\\=|href=[\\s]+" -> "href="
//  )
//
//  for ((key, value) <- patterns) {
//    html = key.r.replaceAllIn(html, value)
//  }
//
//  //  val h1: String = "[\\\\n]".r.replaceAllIn(html, "")
//  //  val h2: String = "href[\\s]+\\=[\\s]+|href[\\s]+\\=|href=[\\s]+".r.replaceAllIn(h1, "href=")
//  val htmlFinal = "(href=[\"|\'](.*?)[\"|\'])".r.replaceAllIn(html, row => {
//    """href="http://test.com.br?%s"""".format(row.group(2))
//  })
//
//  println(htmlFinal)


  //  for(rgx <- pattern.findAllMatchIn(html)) {
  //    println(rgx.group(2))
  //  }


}


