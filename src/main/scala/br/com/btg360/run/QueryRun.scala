package br.com.btg360.run

import br.com.btg360.repositories.QueueRepository

object QueryRun extends App {

  try {

    val repository = new QueueRepository()
    val rules = repository.findAll(14, List(1, 3))

    for (rule <- rules) {
      val rule2 = rule.parse()
      println(rule2.ruleName + " - " + rule2.userRuleId + " - "+ rule2.priority + " -> "+ rule2.today)
    }


  } catch {
    case e: Exception => println(e.printStackTrace())
  }


}


