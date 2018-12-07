package br.com.btg360.application

import scala.reflect.ClassTag

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

  /**
    * @param stringValue
    * @return String
    */
  object String_ {
    implicit class StringSupplement(stringValue: String) {
      def replacement(find: List[String], replace: List[String]): String = {
        var sStringList = stringValue.map(_.toString).toList
        sStringList.map(
          c => {
            var cOut = c
            var i=0
            while(i < find.length) {
              if(cOut == find(i)) {
                cOut = replace(i)
              }
              i += 1
            }
            cOut
          }
        ).mkString("")
      }
      def hasValue: Boolean = {
        if(stringValue == None || stringValue == null || stringValue.isEmpty) {
          return false
        }
        true
      }
    }
  }

  object Any_ {
    implicit class AnySupplement(anyValue: Any) {
      def castAnyToInt: Int = {
        var intValue: Int = 0
        if(anyValue == null || anyValue.toString.isEmpty || anyValue == None) return intValue
        anyValue.toString.toInt
      }
    }
  }

}
