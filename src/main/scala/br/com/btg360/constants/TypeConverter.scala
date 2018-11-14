package br.com.btg360.constants

object TypeConverter {

  /**
    * Validate field cases comes with none and returns the default value
    *
    * @param Any value
    * @param Any defaultValue
    * @return String
    */
  private def noneTo(value: Any, defaultValue: Any): String = {
    if (value == None || value == null || value.toString.isEmpty) {
      return defaultValue.toString
    }

    value.toString
  }

  /**
    * @param value
    * @return String
    */
  def toString(value: Any): String = {
    this.noneTo(value, "")
  }

  /**
    * @param value
    * @return Int
    */
  def toInt(value: Any): Int = {
    this.noneTo(value, 0).toInt
  }

  /**
    * @param value
    * @return Double
    */
  def toDouble(value: Any): Double = {
    this.noneTo(value, 0).toDouble
  }

  /**
    * @param value
    * @return Double
    */
  def toFloat(value: Any): Float = {
    this.noneTo(value, 0).toFloat
  }

  /**
    * @param value
    * @return Double
    */
  def toBoolean(value: Any): Boolean = {
    this.noneTo(value, false).toBoolean
  }

}
