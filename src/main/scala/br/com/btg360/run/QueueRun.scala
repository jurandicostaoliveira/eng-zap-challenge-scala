package br.com.btg360.run

import br.com.btg360.repositories.RuleRepository

object QueueRun extends App {

  val repository: RuleRepository = new RuleRepository()

  val rules = repository.findActiveByUserId(14)

  for(rule <- rules) {
    val a = rule.parse
    println(a.id +  " -> " + a.priority)
  }


}
