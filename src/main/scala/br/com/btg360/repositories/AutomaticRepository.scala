package br.com.btg360.repositories

import br.com.btg360.application.Repository
import br.com.btg360.entities.{FilterEntity, StockEntity}
import br.com.btg360.jdbc.MySqlAllin
import br.com.btg360.services.{PeriodService, ReferenceListService}
import br.com.btg360.spark.SparkCoreSingleton
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

class AutomaticRepository extends Repository {

  import String_._

  private val db = new MySqlAllin()

  val sc = SparkCoreSingleton.getContext

  val periodService: PeriodService = new PeriodService()

  var allinId: Int = _

  var listId: Int = _

  var listExclusionId: Int = _

  var field: String = _

  var formatField: String = _

  var filterId: Int = _

  var interval: Int = _

  var frequency: Int = _

  var filters: String = _

  def setAllinId(allinId: Int): AutomaticRepository = {
    this.allinId = allinId
    this
  }

  def setListId(listId: Int): AutomaticRepository = {
    this.listId = listId
    this
  }

  def setListExclusionId(listExclusionId: Int): AutomaticRepository = {
    this.listExclusionId = listExclusionId
    this
  }

  def setField(field: String): AutomaticRepository = {
    this.field = field
    this
  }

  def setFormatField(formatField: String): AutomaticRepository = {
    this.formatField = formatField
    this
  }

  def setFilterId(filterId: Int): AutomaticRepository = {
    this.filterId = filterId
    this
  }

  def setInterval(interval: Int): AutomaticRepository = {
    this.interval = interval
    this
  }

  def setFrequency(frequency: Int): AutomaticRepository = {
    this.frequency = frequency
    this
  }

  def setFilters(filters: String): AutomaticRepository = {
    this.filters = filters
    this
  }

  def hasReferenceList: Boolean = {
    new ReferenceListService().exists(this.listId)
  }

  def getListTable: String = {
    "emailpro_lista.base_%d_%d".format(this.allinId, this.listId)
  }

  def getExclusionListTable: String = {
    "emailpro_lista.base_%d_%d".format(this.allinId, this.listExclusionId)
  }

  def getActivityTable: String = {
    "emailpro_atividade.atividade_%d".format(this.allinId)
  }

  def hasExclusionList(query: String): String = {
    var whereNotIn: String = ""
    if (
      (new ReferenceListService().exists(this.listExclusionId)) &&
        (this.listExclusionId != this.listId)) {
      whereNotIn =
        s"""
            AND NOT IN(SELECT nm_email FROM ${this.getExclusionListTable})
          """

    }
    query.concat(whereNotIn)
  }

  def hasFilters(query: String): String = {
    var whereRaw = ""
    if (this.filters.hasValue) {
      whereRaw = whereRaw.concat(" AND ").concat(this.filters)
    }
    query.concat(whereRaw)
  }

  def getFormatToLike: String = {
    val formats: Map[String, String] = Map(
      "m-d" -> "-MM-dd",
      "d-m" -> "dd-MM-",
      "d/m" -> "dd/MM/",
      "j/n" -> "d/M/"
    )
    if (formats(this.formatField).hasValue) formats(this.formatField) else "-MM-dd"
  }

  def getFormatToYear: String = {
    val formats: Map[String, String] = Map(
      "m-d" -> "yyyy-MM-dd",
      "d-m" -> "dd-MM-yyyy",
      "d/m" -> "dd/mm/yyyy",
      "j/n" -> "d/M/yyyy"
    )
    if (formats(this.formatField).hasValue) formats(this.formatField) else "yyyy-MM-dd"
  }

  def testFilter: RDD[String] = {
    try {
      val df: DataFrame = this.db.sparkRead(
        """(SELECT * FROM emailpro_lista.base_7018_2127908 WHERE DT_NASCIMENTO LIKE "1981-11-%" LIMIT 1) emp_alias"""
      )
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
            cFGrupos.id_filter_data = fData.id_filter_data) filters
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

  def queryFilterAppend(query: String): String = {
    var queryResult = ""
    queryResult = this.hasExclusionList(query)
    queryResult = this.hasFilters(queryResult)
    queryResult = queryResult.concat(") list")
    queryResult
  }

  def findBirthday: RDD[(String, StockEntity)] = {
    try {
      if (!this.hasReferenceList) {
        return sc.emptyRDD[(String, StockEntity)]
      }
      val period = periodService.format(this.getFormatToLike).now

      var query: String =
        s"""
          (SELECT * FROM ${this.getListTable}
          WHERE ${this.field} LIKE "%${period}%"
         """
      query = this.queryFilterAppend(query)
      val df: DataFrame = this.db.sparkRead(query)
      this.dfToRDD(df)
    } catch {
      case e: Exception => e.getLocalizedMessage
        sc.emptyRDD[(String, StockEntity)]
    }
  }

  def findSendingDate: RDD[(String, StockEntity)] = {
    try {
      if (!this.hasReferenceList) {
        return sc.emptyRDD[(String, StockEntity)]
      }
      val period = periodService.format(this.getFormatToYear).timeByDay(-this.interval)
      var query =
        s"""
           (SELECT * FROM ${this.getListTable}
           WHERE ${this.field} = "${period}"
        """
      query = this.queryFilterAppend(query)
      val df: DataFrame = this.db.sparkRead(query)
      this.dfToRDD(df)
    } catch {
      case e: Exception => e.getLocalizedMessage
        sc.emptyRDD[(String, StockEntity)]
    }
  }

  def findInactive: RDD[(String, StockEntity)] = {
    try {
      if (!this.hasReferenceList) {
        return sc.emptyRDD[(String, StockEntity)]
      }

      val listTable: String = this.getListTable
      val activityTable: String = this.getActivityTable
      val period = periodService.format("yyyy-MM-dd").timeByDay(-this.interval)

      var query =
        s"""
           (SELECT lTable.* FROM ${listTable} as lTable
           INNER JOIN ${activityTable} as acTable
           ON acTable.nm_email = lTable.nm_email
           WHERE
           acTable.dt_atividade = ${period}
         """
      query = this.queryFilterAppend(query)
      val df: DataFrame = this.db.sparkRead(query)
      this.dfToRDD(df)
    } catch {
      case e: Exception => e.getLocalizedMessage
        sc.emptyRDD[(String, StockEntity)]
    }
  }

  def dfToRDD(df: DataFrame): RDD[(String, StockEntity)] = {
    df.rdd.map(row => {
      val map = row.schema.fieldNames.map(field => field -> row.getAs[Any](field)).toMap
      val stockEntity = new StockEntity(references = map)
      (row.getAs("nm_email").toString.trim, stockEntity)
    })
  }

}
