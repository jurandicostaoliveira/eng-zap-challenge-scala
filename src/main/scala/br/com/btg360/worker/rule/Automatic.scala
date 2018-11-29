package br.com.btg360.worker.rule

import br.com.btg360.constants.{Automatic, QueueStatus, Rule}
import br.com.btg360.entities.StockEntity
import br.com.btg360.repositories.AutomaticRepository
import br.com.btg360.services.AutomaticService
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD

class Automatic extends RuleTrait {

  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.AUTOMATIC_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {

    println("LIST_ID: " + this.queue.rule.automatics.list)
    println("FILTER: " + this.queue.rule.automatics.filter)
    println("FORMAT: " + this.queue.rule.automatics.format)
    println("ALLIND: " + this.queue.rule.allinId)
    println("FILTER_ID: " + this.queue.rule.filterId)

    this.queue.ruleName match {
      case Automatic.BIRTHDAY =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)

        val automaticService: AutomaticService = new AutomaticService
        automaticService.filter(this.queue)

      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)

      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
    }

//    val autoRepository: AutomaticRepository = new AutomaticRepository
//    autoRepository.testFilter

    null
  }

  /**
    * Other methods
    */

}
