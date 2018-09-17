package br.com.btg360.traits

trait JobTrait {

  def dispatch(userId : String) : Unit

  def asyncDispatch : Unit

}
