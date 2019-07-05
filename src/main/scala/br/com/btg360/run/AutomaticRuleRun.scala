package br.com.btg360.run

import br.com.btg360.entities.{QueueEntity, RuleDataEntity, StockEntity}
import br.com.btg360.worker.rule.Automatic
import org.apache.spark.rdd.RDD

//object AutomaticRuleRun extends Application with App {
//  import Any_._
object AutomaticRuleRun extends App {

//  var queueEntity = new QueueEntity()
//
//  queueEntity.userRuleId = 83
//  queueEntity.today = "2019-07-05"
//  queueEntity.userId = 6
//  queueEntity.ruleTypeId = 3
//  queueEntity.ruleName = "inactive"
//  queueEntity.dataStringJson = "{  \n   \"configs\":{  \n      \"ruleName\":\"inativos_btg\",\n      \"subject\":\"Voc\\u00ea tem acompanhados nossas inova\\u00e7\\u00f5es?\",\n      \"senderName\":\"Polishop\",\n      \"senderEmail\":\"ofertas@envio.polishop.com.br\",\n      \"emailResponsible\":\"bruno.miquelini@polishop.com.br\",\n      \"hour\":\"15:00:00\",\n      \"interval\":30\n   },\n   \"module\":{  \n      \"selected\":\"\",\n      \"quantity\":12,\n      \"percentMin\":30,\n      \"percentMax\":70\n   },\n   \"html\":{  \n      \"template\":\"\",\n      \"theme\":\"\",\n      \"layout\":2,\n      \"content\":\"\"\n   },\n   \"channels\":{  \n      \"email\":{  \n         \"status\":true,\n         \"utms\":[  \n            {  \n               \"key\":\"utm_medium\",\n               \"value\":\"email\"\n            },\n            {  \n               \"key\":\"utm_campaign\",\n               \"value\":\"btg_inativos\"\n            },\n            {  \n               \"key\":\"utm_source\",\n               \"value\":\"btg360\"\n            },\n            {  \n               \"key\":\"utm_content\",\n               \"value\":\"inativos\"\n            }\n         ]\n      },\n      \"facebook\":{  \n         \"status\":false,\n         \"utms\":[  \n\n         ]\n      },\n      \"push\":{  \n         \"status\":false,\n         \"utms\":[  \n\n         ]\n      }\n   },\n   \"automatics\":{  \n      \"list\":2191663,\n      \"exclusion\":\"\",\n      \"filter\":\"\",\n      \"field\":\"\",\n      \"format\":\"m-d\"\n   },\n   \"rule\":{  \n      \"ruleId\":19,\n      \"typeId\":3,\n      \"typeName\":\"auto\",\n      \"typeAlias\":\"Autom\\u00e1ticas\",\n      \"name\":\"Inativos\",\n      \"alias\":\"inactive\",\n      \"peal\":0,\n      \"module\":0,\n      \"description\":\"Envios autom\\u00e1ticos de acordo com o per\\u00edodo de inatividade dos usu\\u00e1rios com suas campanhas de e-mail marketing\",\n      \"minDescription\":\"Envios autom\\u00e1ticos de acordo com o per\\u00edodo de inatividade dos usu\\u00e1rios com suas campanhas de e-mail marketing\",\n      \"techDescription\":\"Disparos realizados com informa\\u00e7\\u00f5es de listas. Ou seja, sem a necessidade de configura\\u00e7\\u00e3o de TAGs\",\n      \"period\":1,\n      \"icon\":\"flaticon-healthy\",\n      \"priority\":1,\n      \"groupId\":17\n   },\n   \"account\":{  \n      \"id\":6,\n      \"btgId\":6347,\n      \"allinId\":7131,\n      \"transId\":5998,\n      \"token\":\"2e6f9b0d5885b6010f9167787445617f553a735p\"\n   }\n}"
//  queueEntity.parse
//
//  var data : RDD[(String, StockEntity)] = new Automatic(queueEntity).getData
//
//  println("gdfgdfgdf"+data.count())




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
