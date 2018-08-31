package traits

trait JobTrait {

  def dispatch(userId : String) : Unit

  def asyncDispatch : Unit

}
