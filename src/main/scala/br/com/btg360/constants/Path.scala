package br.com.btg360.constants

import java.io.File

object Path {

  //val PROJECT: String = new File(".").getCanonicalPath

  val PROJECT: String = "/home/Btg-Scala-Sending-Generator"

  val STORAGE: String = "%s/storage".format(PROJECT)

  //val LOGS: String = "%s/logs".format(STORAGE)
  val LOGS: String = "/storage/logs"

}
