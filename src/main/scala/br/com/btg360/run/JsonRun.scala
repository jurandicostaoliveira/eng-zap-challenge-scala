package br.com.btg360.run


//import scala.util.parsing.json._
//import scala.collection.mutable.Map

import com.google.gson.Gson

object JsonRun extends App {

  val stringJson: String =
    """
      {
        "languages": [{
            "name": "English",
            "is_active": true,
            "completeness": 2.5
        }, {
            "name": "Latin",
            "is_active": false,
            "completeness": 0.9
        }]
      }
    """


}
