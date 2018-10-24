package br.com.btg360.application


abstract class Application extends Serializable {

  /**
    * Return a new instance of a class
    *
    * @param beanClass
    * @tparam T
    * @return T instance
    */
  def invoke[T](beanClass: Class[T]): T = {
    beanClass.newInstance()
  }

}
