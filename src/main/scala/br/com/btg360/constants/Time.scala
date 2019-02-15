package br.com.btg360.constants

import br.com.btg360.services.PeriodService

object Time {

  /**
    * @return Boolean
    */
  def isMidnight : Boolean = {
    if (new PeriodService("HH").now == "00") true else false
  }

}
