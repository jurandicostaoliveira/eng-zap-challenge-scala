package services

import java.io.InputStream
import java.util.Properties

class PropService {

  def get(fileName : String) : Properties = {
    val properties : Properties = new Properties()
    var inputStream : InputStream = null
    try {
      inputStream = getClass.getClassLoader.getResourceAsStream(fileName)
      if (inputStream != null) {
        properties.load(inputStream)
      }
      properties
    } catch {
      case e : Exception => e.printStackTrace()
        null
    }
  }
}
