package br.com.btg360.worker.rule

import br.com.btg360.constants.Automatic
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.AutomaticRepository
import br.com.btg360.services.AutomaticService
import org.apache.commons.validator.EmailValidator
import org.apache.spark.rdd.RDD

class Automatic(queue: QueueEntity) {

  /**
    * @return AutomaticRepository
    */
  private def getAutomaticRuleModel: AutomaticRepository = {
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
  def getData: RDD[(String, StockEntity)] = {
    var rdd: RDD[(String, StockEntity)] = this.getAutomaticRuleModel.sc.emptyRDD[(String, StockEntity)]
    this.queue.ruleName match {
      case Automatic.BIRTHDAY =>
        rdd = this.birthday
      case Automatic.SENDING_DATE =>
        rdd = this.sendingDate
      case Automatic.SENDING_DATE =>
        rdd = this.inactive
    }
    rdd
  }

  /**
    * Birthday rule
    *
    * @return RDD
    */
  private def birthday: RDD[(String, StockEntity)] = {
    this.getAutomaticRuleModel.findBirthday.filter(row => {
      EmailValidator.getInstance().isValid(row._1)
    })
  }

  /**
    * Sending date rule
    *
    * @return RDD
    */
  private def sendingDate: RDD[(String, StockEntity)] = {
    this.getAutomaticRuleModel.findSendingDate.filter(row => {
      EmailValidator.getInstance().isValid(row._1)
    })
  }

  /**
    * Inactive rule
    *
    * @return RDD
    */
  private def inactive: RDD[(String, StockEntity)] = {
    this.getAutomaticRuleModel.findInactive.filter(row => {
      EmailValidator.getInstance().isValid(row._1)
    })
  }
}
