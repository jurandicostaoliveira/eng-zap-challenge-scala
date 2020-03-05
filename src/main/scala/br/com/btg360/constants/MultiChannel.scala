package br.com.btg360.constants

object MultiChannel {

  val STATUS: Int = 1

  /**
    * Validation to run in dedicated environment
    *
    * @return Int
    */
  def isDedicatedEnv: Int = {
    if (Environment.isHomologation) {
      return 1
    }
    0
  }

}
