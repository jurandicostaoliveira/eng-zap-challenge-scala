package br.com.btg360.entities

class QueueEntity {

  private[this] var _userRuleId: Int = _

  private[this] var _userId: Int = _

  private[this] var _groupId: Int = _

  private[this] var _ruleTypeId: Int = _

  private[this] var _ruleActionName: Int = _

  private[this] var _isPeal: Int = _

  private[this] var _priority: Int = _

  private[this] var _status: Int = _

  private[this] var _consolidatedTableName: String = _

  private[this] var _sendLimit: Int = _

  private[this] var _vmta: String = _

  def vmta: String = _vmta

  def vmta_=(value: String): Unit = {
    _vmta = value
  }

  def sendLimit: Int = _sendLimit

  def sendLimit_=(value: Int): Unit = {
    _sendLimit = value
  }

  def consolidatedTableName: String = _consolidatedTableName

  def consolidatedTableName_=(value: String): Unit = {
    _consolidatedTableName = value
  }

  def status: Int = _status

  def status_=(value: Int): Unit = {
    _status = value
  }

  def priority: Int = _priority

  def priority_=(value: Int): Unit = {
    _priority = value
  }

  def isPeal: Int = _isPeal

  def isPeal_=(value: Int): Unit = {
    _isPeal = value
  }

  def ruleActionName: Int = _ruleActionName

  def ruleActionName_=(value: Int): Unit = {
    _ruleActionName = value
  }

  def ruleTypeId: Int = _ruleTypeId

  def ruleTypeId_=(value: Int): Unit = {
    _ruleTypeId = value
  }

  def groupId: Int = _groupId

  def groupId_=(value: Int): Unit = {
    _groupId = value
  }

  def userId: Int = _userId

  def userId_=(value: Int): Unit = {
    _userId = value
  }

  def userRuleId: Int = _userRuleId

  def userRuleId_=(value: Int): Unit = {
    _userRuleId = value
  }
}
