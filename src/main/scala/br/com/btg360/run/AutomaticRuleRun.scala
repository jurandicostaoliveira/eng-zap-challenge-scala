package br.com.btg360.run

import br.com.btg360.worker.rule.Automatic

object AutomaticRuleRun extends App {

  new Automatic().dispatch(14)

//  var stringList:  List[String] = "d-m".map(_.toString).toList
//
//  var l: String = stringList.mkString("")
//
//  println("SAIDA: " + l)

//  val commandExecutor: Map[String, Function1[Int, Unit]] = Map(
//    "cleanup" -> {(i: Int)=> println("cleanup successfully => " + i)},
//    "cleanup2" -> {(j: Int)=> println("cleanup2 successfully => " + j)},
//    "cleanup3" -> {(h: Int)=> test(h)}
//    )
//  val command="cleanup"
//  val command2="cleanup2"
//  val command3="cleanup3"
//  commandExecutor(command).apply(10)
//  commandExecutor(command2).apply(11)
//  commandExecutor(command3).apply(20)
//
//  def test(h: Int): Unit = {
//    println("cleanup3 successfully => " + h)
//  }
}
