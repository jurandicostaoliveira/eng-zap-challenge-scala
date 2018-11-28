package br.com.btg360.run

import br.com.btg360.constants.TypeConverter
import br.com.btg360.repositories.TransactionalRepository
import br.com.btg360.services.PeriodService
import br.com.btg360.worker.rule.Daily

import scala.collection.mutable.HashMap
import scala.util.Random

object DailyRuleRun extends App {

  val random = new Random()

  val number: Long = random.nextInt((50000 - 1000) * 40)
  println(number)

  //new Daily().dispatch(14)

//  new TransactionalRepository().transactionalId(20000)
//    .insertBatch("lala", List(HashMap("name" -> "teste", "id" -> 768)))

}


