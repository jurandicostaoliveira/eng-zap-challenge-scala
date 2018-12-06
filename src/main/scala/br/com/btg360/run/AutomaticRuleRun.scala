package br.com.btg360.run

import br.com.btg360.application.Application
import br.com.btg360.worker.rule.Automatic

import scala.collection.mutable.ListMap
import scala.reflect.ClassTag

//object AutomaticRuleRun extends Application with App {
//  import Any_._
object AutomaticRuleRun extends App {
  new Automatic().dispatch(14)

//  var list: Any = None
//
//  var listId: Int = _
//
//  listId = list.castAnyToInt

//  if(ClassTag(list.getClass).toString() == "java.lang.Integer"
//    || ClassTag(list.getClass).toString() == "Int") {
//    listId = list.asInstanceOf[Int]
//  } else {
//    listId = list.asInstanceOf[String].toInt
//  }

//  println(listId)

//  val listString: List[String] = List("ola", "mundo", "", "vazio")
//  var strConcat: String = ""
//  listString.foreach(item => {
//    strConcat = strConcat.concat(if(item != "") item.trim else "-")
//  })
//  println(strConcat)

//  var mapGroups: Map[Int, List[Map[String, String]]] = Map.empty[Int, List[Map[String, String]]]
//  var groups: Map[String, String] = Map.empty[String, String]
//  var listGroups: List[Map[String, String]] = List.empty[Map[String, String]]
//
//  groups += (
//    "condition" -> "condition value1",
//    "operator" -> ">=",
//    "groupOperator" -> "and")
//
//  listGroups :+= groups
//
//  mapGroups += (10 -> listGroups)
//
//  listGroups = List.empty[Map[String, String]]
//
//  groups += (
//    "condition" -> "condition value2",
//    "operator" -> "<=",
//    "groupOperator" -> "or")
//
//  listGroups :+= groups
//
//  mapGroups += (20 -> listGroups)
//
//  mapGroups.foreach(group => {
//    group._2.foreach(item => {
//      println(item.get("condition").get)
//    })
//  })


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
