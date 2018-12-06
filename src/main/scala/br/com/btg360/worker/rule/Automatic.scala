package br.com.btg360.worker.rule

import br.com.btg360.application.Application
import br.com.btg360.constants.{Automatic, QueueStatus, Rule}
import br.com.btg360.entities.StockEntity
import br.com.btg360.repositories.AutomaticRepository
import br.com.btg360.services.AutomaticService
import br.com.btg360.traits.RuleTrait
import org.apache.spark.rdd.RDD

class Automatic extends RuleTrait{
  /**
    * @return List[Int]
    */
  override def getTypes: List[Int] = List(Rule.AUTOMATIC_ID)

  /**
    * @return Int
    */
  override def getCompletedStatus: Int = QueueStatus.FINALIZED

  def getAutomaticRuleModel: AutomaticRepository = {
    val automaticService: AutomaticService = new AutomaticService
    val automaticRepository: AutomaticRepository = new AutomaticRepository
    automaticRepository
      .setAllinId(this.queue.rule.allinId)
      .setListId(this.queue.rule.listId)
      .setListExclusionId(this.queue.rule.listExclusionId)
      .setField(this.queue.rule.field)
      .setFormatField(this.queue.rule.formatField)
      .setFilterId(this.queue.rule.filterId)
      .setInterval(this.queue.rule.interval)
      .setFrequency(this.queue.rule.frequency)
      .setFilters(automaticService.filter(this.queue))
  }

  /**
    * @return RDD
    */
  override def getData: RDD[(String, StockEntity)] = {

    println("LIST_ID: " + this.queue.rule.automatics.list)
    println("FILTER: " + this.queue.rule.automatics.filter)
    println("FORMAT: " + this.queue.rule.automatics.format)
    println("ALLIND: " + this.queue.rule.allinId)
    println("FILTER_ID: " + this.queue.rule.filterId)
    println("LIST_EXCLUSION_ID: " + this.queue.rule.listExclusionId)

    this.queue.ruleName match {
      case Automatic.BIRTHDAY =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)

        val rdd: RDD[(String, StockEntity)] = this.getAutomaticRuleModel.findBirthday
        rdd.foreach(row => {
          println(row._1)
        })

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
