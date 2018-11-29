package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.entities.FilterEntity
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.spark.{SparkCoreSingleton, SparkSqlSingleton}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import sun.awt.X11.XErrorHandlerUtil

class AutomaticRepository extends Repository {

  private val sc = SparkCoreSingleton.getContext

  private val db = new MySqlAllin()

  var allinId: Int = _

  var filterId: Int = _

  def setAllinId(_allinId: Int): AutomaticRepository = {
    this.allinId = _allinId
    this
  }

  def setFilterId(_filterId: Int): AutomaticRepository = {
    this.filterId = _filterId
    this
  }

  def testFilter: RDD[String] = {
    try {
      val df: DataFrame = this.db.sparkRead(
        """(SELECT * FROM emailpro_lista.base_7018_2127908 WHERE DT_NASCIMENTO LIKE "1981-11-%" LIMIT 1) emp_alias"""
      )

      println("PRINTA DATAFRAME: ")
      df.show(20)
    } catch {
      case e: Exception => println(e.printStackTrace())
    }
    null
  }

  def findFilters: RDD[FilterEntity] = {
    try {
      val query: String =
        s"""
          (SELECT
            fData.id_filter_data AS id,
            fData.nm_field AS field,
            fData.nm_filter_type AS comparator,
            fData.nm_filter AS value,
            fData.formato AS format,
            cFGrupos.id_grupo AS groupId,
            cFGrupos.operador AS operator,
            cGrupos.operador_grupos AS groupOperator
          FROM
            emailpro_mailsender.filter_data_${this.allinId} as fData
          INNER JOIN
            emailpro_mailsender.cor_grupos as cGrupos
          ON
            cGrupos.id_filter = fData.id_filter
          INNER JOIN
            emailpro_mailsender.cor_filter_grupos as cFGrupos
          ON
            cFGrupos.id_grupo = cGrupos.id_grupo
          WHERE
            cFGrupos.id_login = ${this.allinId}
          AND
            fData.id_filter = ${this.filterId}
          AND
            cFGrupos.id_filter_data = fData.id_filter_data) as filters
        """
      val df: DataFrame = this.db.sparkRead(query)

      df.rdd.map[FilterEntity](row => {
        new FilterEntity().setRow(row)
      })
    } catch {
      case e: Exception => println(e.printStackTrace())
        sc.emptyRDD[FilterEntity]
    }
  }
}
