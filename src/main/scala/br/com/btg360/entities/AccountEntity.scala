package br.com.btg360.entities

import scala.util.parsing.json.{JSONArray, JSONObject}

@SerialVersionUID(1L)
class AccountEntity extends Serializable {

  private[this] var _accountId: Int = _

  private[this] var _accountAllinId: Int = _

  private[this] var _ruleId: Int = _

  private[this] var _rulePealId: Int = _

  private[this] var _ruleName: String = _

  private[this] var _ruleTypeId: Int = _

  private[this] var _periodId: Int = 1

  private[this] var _interval: Int = _

  private[this] var _purchaseInterval: Int = 10

  private[this] var _maxProducts: Int = 10

  private[this] var _totalFiltersBought: Int = _

  private[this] var _totalFiltersSend: Int = _

  private[this] var _fidelityType: Int = _

  private[this] var _fidelityValue: Int = _

  private[this] var _layoutType: Int = _

  private[this] var _timeInactivity: Int = _

  private[this] var _isPeal: Boolean = false

  @transient private[this] var _boughtFilterOptions: JSONObject = _

  @transient private[this] var _toSendFilterOptions: JSONObject = _

  @transient private[this] var _boughtFilterValues: JSONArray = _

  @transient private[this] var _toSendFilterValues: JSONArray = _

  @transient private[this] var _recurrenceConfig: JSONObject = _

  private[this] var _channels: List[String] = List()

  def channels: List[String] = _channels

  def channels_=(value: List[String]): Unit = {
    _channels = value
  }

  def recurrenceConfig: JSONObject = _recurrenceConfig

  def recurrenceConfig_=(value: JSONObject): Unit = {
    _recurrenceConfig = value
  }

  def toSendFilterValues: JSONArray = _toSendFilterValues

  def toSendFilterValues_=(value: JSONArray): Unit = {
    _toSendFilterValues = value
  }

  def boughtFilterValues: JSONArray = _boughtFilterValues

  def boughtFilterValues_=(value: JSONArray): Unit = {
    _boughtFilterValues = value
  }

  def toSendFilterOptions: JSONObject = _toSendFilterOptions

  def toSendFilterOptions_=(value: JSONObject): Unit = {
    _toSendFilterOptions = value
  }

  def boughtFilterOptions: JSONObject = _boughtFilterOptions

  def boughtFilterOptions_=(value: JSONObject): Unit = {
    _boughtFilterOptions = value
  }

  def isPeal: Boolean = _isPeal

  def isPeal_=(value: Boolean): Unit = {
    _isPeal = value
  }

  def timeInactivity: Int = _timeInactivity

  def timeInactivity_=(value: Int): Unit = {
    _timeInactivity = value
  }

  def layoutType: Int = _layoutType

  def layoutType_=(value: Int): Unit = {
    _layoutType = value
  }

  def fidelityValue: Int = _fidelityValue

  def fidelityValue_=(value: Int): Unit = {
    _fidelityValue = value
  }

  def fidelityType: Int = _fidelityType

  def fidelityType_=(value: Int): Unit = {
    _fidelityType = value
  }

  def totalFiltersSend: Int = _totalFiltersSend

  def totalFiltersSend_=(value: Int): Unit = {
    _totalFiltersSend = value
  }

  def totalFiltersBought: Int = _totalFiltersBought

  def totalFiltersBought_=(value: Int): Unit = {
    _totalFiltersBought = value
  }

  def maxProducts: Int = _maxProducts

  def maxProducts_=(value: Int): Unit = {
    _maxProducts = value
  }

  def purchaseInterval: Int = _purchaseInterval

  def purchaseInterval_=(value: Int): Unit = {
    _purchaseInterval = value
  }

  def interval: Int = _interval

  def interval_=(value: Int): Unit = {
    _interval = value
  }

  def periodId: Int = _periodId

  def periodId_=(value: Int): Unit = {
    _periodId = value
  }

  def ruleTypeId: Int = _ruleTypeId

  def ruleTypeId_=(value: Int): Unit = {
    _ruleTypeId = value
  }

  def ruleName: String = _ruleName

  def ruleName_=(value: String): Unit = {
    _ruleName = value
  }

  def rulePealId: Int = _rulePealId

  def rulePealId_=(value: Int): Unit = {
    _rulePealId = value
  }

  def ruleId: Int = _ruleId

  def ruleId_=(value: Int): Unit = {
    _ruleId = value
  }

  def accountAllinId: Int = _accountAllinId

  def accountAllinId_=(value: Int): Unit = {
    _accountAllinId = value
  }

  def accountId: Int = _accountId

  def accountId_=(value: Int): Unit = {
    _accountId = value
  }
}
