package br.com.btg360.traits

trait RuleTrait {

  def configuration : String

  def dispatch(): Unit = {
    println(this.configuration)
  }

}
