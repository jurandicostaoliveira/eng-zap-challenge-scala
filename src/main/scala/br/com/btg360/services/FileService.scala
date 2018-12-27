package br.com.btg360.services

import br.com.btg360.application.Service

import scala.reflect.io.{Directory}
import java.io.File

class FileService extends Service {

  /**
    * @param String directoryPath
    */
  def deleteRecursive(directoryPath: String): Unit = {
    try {
      new Directory(new File(directoryPath)).deleteRecursively()
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
  }

}
