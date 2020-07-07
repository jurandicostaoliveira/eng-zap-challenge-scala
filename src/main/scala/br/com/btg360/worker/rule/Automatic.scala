package br.com.btg360.worker.rule

import br.com.btg360.constants.{Automatic => AT}
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.{AutomaticChannelManagerRepository, AutomaticRepository}
import br.com.btg360.services.AutomaticService
import org.apache.commons.validator.EmailValidator
import org.apache.spark.rdd.RDD

class Automatic(queue: QueueEntity) {

  /**
    * @return AutomaticChannelManagerRepository
    */
  private def getAutomaticChannelManagerRepository: AutomaticChannelManagerRepository = {
    val automaticChannelManagerRepository: AutomaticChannelManagerRepository = new AutomaticChannelManagerRepository
    automaticChannelManagerRepository
      .setAllinId(this.queue.rule.allinId)
      .setListId(this.queue.rule.listId)
      .setListExclusionId(this.queue.rule.listExclusionId)
      .setField(this.queue.rule.field)
      .setFilterId(this.queue.rule.filterId)
      .setInterval(this.queue.rule.interval)
  }

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
    * @return bool
    */
  private def isMigratedChannelManager: Boolean = {
    this.getAutomaticChannelManagerRepository.isMigratedChannelManager
  }

  /**
    * @return RDD
    */
  def getData: RDD[(String, StockEntity)] = {
    try {
      this.queue.ruleName match {
        case AT.BIRTHDAY => this.birthday
        case AT.SENDING_DATE => this.sendingDate
        case AT.INACTIVE => this.inactive
        case _ => this.getAutomaticRuleModel.sc.emptyRDD[(String, StockEntity)]
      }
    } catch {
      case e: Exception => println(e.printStackTrace())
        this.getAutomaticRuleModel.sc.emptyRDD[(String, StockEntity)]
    }
  }

  /**
    * Birthday rule
    *
    * @return RDD
    */
  private def birthday: RDD[(String, StockEntity)] = {

    if( this.isMigratedChannelManager ) {

      return this.getAutomaticChannelManagerRepository.findBirthday.filter(row => {
        EmailValidator.getInstance().isValid(row._1)
      })
    }

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

    if( this.isMigratedChannelManager ) {

      return this.getAutomaticChannelManagerRepository.findSendingDate.filter(row => {
        EmailValidator.getInstance().isValid(row._1)
      })
    }

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

    if( this.isMigratedChannelManager ) {

      return this.getAutomaticChannelManagerRepository.findInactive(
        this.getAutomaticRuleModel.findInactiveMultichannel
      ).filter(row => {
        EmailValidator.getInstance().isValid(row._1)
      })
    }

    this.getAutomaticRuleModel.findInactive.filter(row => {
      EmailValidator.getInstance().isValid(row._1)
    })
  }
}
