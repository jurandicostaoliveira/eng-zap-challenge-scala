package br.com.btg360.entities

import org.apache.spark.sql.Row

class ConsolidatedEntity extends Serializable {

  private var _userId: String = _

  private var _userSent: String = _

  private var _productId: String = _

  private var _isSent: Int = 0

  private var _isRecommendation: Int = 0

  private var _percentage: Double = 0

  private var _platformId: Int = 0

  private var _createdAt: String = _

  /**
    * Setters attributes
    *
    * @param Row row
    * @return this
    */
  def setRow(row: Row): ConsolidatedEntity = {
    this._userId = row.getAs("userId")
    this._userSent = row.getAs("userSent")
    this._productId = row.getAs("productId")
    this._isSent = row.getAs("isSent")
    this._isRecommendation = row.getAs("isRecommendation")
    this._percentage = row.getAs("percentage")
    this._platformId = row.getAs("platformId")
    this._createdAt = row.getAs("createdAt")
    this
  }

  /**
    * Getter
    *
    * @return String
    */
  def userId: String = this._userId

  /**
    * Setter
    *
    * @param String value
    */
  def userId_=(value: String): Unit = {
    _userId = value
  }

  /**
    * Getter
    *
    * @return String
    */
  def userSent: String = this._userSent

  /**
    * Setter
    *
    * @param String value
    */
  def userSent_=(value: String): Unit = {
    this._userSent = value
  }

  /**
    * Getter
    *
    * @return String
    */
  def productId: String = this._productId

  /**
    * Setter
    *
    * @param String value
    */
  def productId_=(value: String): Unit = {
    this._productId = value
  }

  /**
    * Getter
    *
    * @return Int
    */
  def isSent: Int = this._isSent

  /**
    * Setter
    *
    * @param Int value
    */
  def isSent_=(value: Int): Unit = {
    this._isSent = value
  }

  /**
    * Getter
    *
    * @return Int
    */
  def isRecommendation: Int = this._isRecommendation

  /**
    * Setter
    *
    * @param Int value
    */
  def isRecommendation_=(value: Int): Unit = {
    this._isRecommendation = value
  }

  /**
    * Getter
    *
    * @return Double
    */
  def percentage: Double = this._percentage

  /**
    * Setter
    *
    * @param Double value
    */
  def percentage_=(value: Double): Unit = {
    this._percentage = value
  }

  /**
    * Getter
    *
    * @return Int
    */
  def platformId: Int = this._platformId

  /**
    * Setter
    *
    * @param Int value
    */
  def platformId_=(value: Int): Unit = {
    this._platformId = value
  }

  /**
    * Getter
    *
    * @return String
    */
  def createdAt: String = this._createdAt

  /**
    * Setter
    *
    * @param String value
    */
  def createdAt_=(value: String): Unit = {
    this._createdAt = value
  }

}
