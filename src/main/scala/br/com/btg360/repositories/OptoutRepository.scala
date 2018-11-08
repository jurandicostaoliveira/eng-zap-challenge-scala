package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.constants.{Database, Table}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

class OptoutRepository extends Repository {

  private val sc = SparkCoreSingleton.getContext

  private val db: MySqlAllin = new MySqlAllin()

  private var _allinId: Int = 0

  /**
    * Getter
    *
    * @return
    */
  def allinId: Int = this._allinId

  /**
    * Setter
    *
    * @param value
    * @return
    */
  def allinId(value: Int): OptoutRepository = {
    this._allinId = value
    this
  }

  /**
    * @return
    */
  private def generateTableName: String = "%s.%s_%d".format(Database.OPTOUT, Table.OPTOUT, this._allinId)

  /**
    * Get all emails
    *
    * @return
    */
  def getEmails: RDD[String] = {
    try {
      val rows: RDD[Row] = this.db.sparkRead(this.generateTableName).select("nm_email").rdd
      rows.map(_.getString(0).trim.toLowerCase)
    } catch {
      case e: Exception => println(e.printStackTrace())
        this.sc.emptyRDD
    }
  }

}
