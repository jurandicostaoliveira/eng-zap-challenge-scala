package br.com.btg360.traits

trait RuleTrait {

  def shouldUseRuleTypes: List[Int]

  def dispatch(): Unit = {
    println(this.shouldUseRuleTypes)
  }

}
