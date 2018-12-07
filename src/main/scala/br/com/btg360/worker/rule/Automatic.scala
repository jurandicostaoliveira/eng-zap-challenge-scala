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
    var rdd: RDD[(String, StockEntity)] = this.getAutomaticRuleModel.sc.emptyRDD[(String, StockEntity)]

    this.queue.ruleName match {
      case Automatic.BIRTHDAY =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
        rdd = this.birthday

        rdd.foreach(row => {
          println("email: " + row._1)
        })

      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
        rdd = this.getAutomaticRuleModel.findSendingDate

      case Automatic.SENDING_DATE =>
        println("REGRA AUTOMATICA: " + this.queue.ruleName)
        rdd = this.getAutomaticRuleModel.findInactive
    }
    rdd
  }

  val emailRegex =
    """^[a-zA-Z0-9\.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$"""
  def birthday: RDD[(String, StockEntity)] = {
    this.getAutomaticRuleModel.findBirthday.filter(row => row._1.matches(emailRegex))
  }

  def sendingDate: RDD[(String, StockEntity)] = {
    null
  }

  def inactive: RDD[(String, StockEntity)] = {
    null
  }

}
