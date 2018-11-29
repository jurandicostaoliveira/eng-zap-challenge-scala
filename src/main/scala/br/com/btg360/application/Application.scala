package br.com.btg360.application

import br.com.btg360.spark.SparkCoreSingleton

abstract class Application extends Serializable {

  val sparkContext = SparkCoreSingleton.getContext

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
