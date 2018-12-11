package br.com.btg360.constants

object TypeConverter {

  val VOID: String = ""

  val ZERO: Int = 0

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
    this.noneTo(value, this.VOID)
  }

  /**
    * @param value
    * @return Int
    */
  def toInt(value: Any): Int = {
    this.noneTo(value, this.ZERO).toInt
  }

  /**
    * @param value
    * @return Double
    */
  def toDouble(value: Any): Double = {
    this.noneTo(value, this.ZERO).toDouble
  }

  /**
    * @param value
    * @return Double
    */
  def toFloat(value: Any): Float = {
    this.noneTo(value, this.ZERO).toFloat
  }

  /**
    * @param value
    * @return Double
    */
  def toBoolean(value: Any): Boolean = {
    this.noneTo(value, false).toBoolean
  }

}
