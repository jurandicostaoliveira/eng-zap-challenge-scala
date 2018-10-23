package br.com.btg360.run

import br.com.btg360.entities.{QueueEntity, RuleDataEntity}
import br.com.btg360.repositories.RuleRepository
import br.com.btg360.services.JsonService

object QueryRun extends App {

  try {

    val repository = new RuleRepository()
    val data = repository.findActiveByUserId(14)

    val queue = new QueueEntity()

    for(d <- data) {
      var dataJson = new JsonService().decode[RuleDataEntity](d.data)
      queue.setRule(dataJson)
    }

    println(queue.ruleName)

  } catch {
    case e: Exception => println(e.printStackTrace())
  }


}


