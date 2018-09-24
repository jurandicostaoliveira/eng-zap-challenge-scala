package br.com.btg360.traits

trait JobTrait {

  def dispatch(userId : Int) : Unit

  def asyncDispatch : Unit

}
