package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.{FilterEntity, QueueEntity}
import br.com.btg360.repositories.AutomaticRepository

import scala.util.control.Breaks._

class AutomaticService extends Service {

  import String_._

  val filters: Map[String, Function1[FilterEntity, String]] = Map(
    "=" -> { (data: FilterEntity) => this.toOperator(data) },
    "!=" -> { (data: FilterEntity) => this.toOperator(data) },
    ">" -> { (data: FilterEntity) => this.toOperator(data) },
    ">=" -> { (data: FilterEntity) => this.toOperator(data) },
    "<" -> { (data: FilterEntity) => this.toOperator(data) },
    "<=" -> { (data: FilterEntity) => this.toOperator(data) },
    "like" -> { (data: FilterEntity) => this.toLike(data) },
    "clike" -> { (data: FilterEntity) => this.toLike(data) },
    "flike" -> { (data: FilterEntity) => this.toLike(data) },
    "not like" -> { (data: FilterEntity) => this.toLike(data) },
    "between_ma" -> { (data: FilterEntity) => this.toBetween(data) },
    "between_me" -> { (data: FilterEntity) => this.toBetween(data) },
    "is null" -> { (data: FilterEntity) => this.toNull(data) },
    "is not null" -> { (data: FilterEntity) => this.toNull(data) },
    "quantdias" -> { (data: FilterEntity) => this.toDay(data) },
    "qdiasanive" -> { (data: FilterEntity) => this.toDay(data) },
    "curdate()" -> { (data: FilterEntity) => this.toCurdate(data) }
  )

  def toOperator(data: FilterEntity): String = {
    String.format("LOWER(%s) %s %s", data.field, data.comparator, data.value.toLowerCase)
  }

  def toLike(data: FilterEntity): String = {
    val s = "%"
    val value: String = data.value.toLowerCase
    val conditions: Map[String, String] = Map(
      "like" -> String.format("%s LIKE '%s%s'", data.field, value, s),
      "clike" -> String.format("%s LIKE '%s%s%s'", data.field, s, value, s),
      "flike" -> String.format("%s LIKE '%s%s'", data.field, s, value),
      "not like" -> String.format("%s NOT LIKE '%s%s%s'", data.field, s, value, s)
    )

    conditions(data.comparator)
  }

  def toBetween(data: FilterEntity): String = {
    val periodService: PeriodService = new PeriodService()
    val now: String = periodService.format(data.format).now
    val from: String = periodService.format(data.format).timeByDay(-data.value.toInt)
    val to: String = periodService.format(data.format).timeByDay(data.value.toInt)
    val format: String = data.format.replacement(List("d", "m", "y"), List("%d", "%m", "%y"))
    val field: String = String.format("STR_TO_DATE(%s, %s)", data.field, format)

    val conditions: Map[String, String] = Map(
      "between_me" -> String.format("%s BETWEEN '%s' AND '%s'", field, from, now),
      "between_ma" -> String.format("%s BETWEEN '%s' AND '%s'", field, now, to)
    )
    conditions(data.comparator)
  }

  def toNull(data: FilterEntity): String = {
    val conditions: Map[String, String] = Map(
      "is null" -> String.format("%s IS NULL OR %s = ''", data.field, data.field),
      "is not null" -> String.format("%s IS NOT NULL OR %s = ''", data.field, data.field)
    )
    conditions(data.comparator)
  }

  def toDay(data: FilterEntity): String = {
    val f = "%m-%d"
    val conditions: Map[String, String] = Map(
      "quantdias" -> String.format("%s = DATE_ADD(date(NOW())), INTERVAL %s%s DAY",
        data.field, data.format, data.value),
      "qdiasanive" -> String.format(
        "DATE_FORMAT(%s, '%s') = DATE_FORMAT(DATE_ADD(date(NOW()), INTERVAL %s%s DAY), '%s')",
        data.field, f, data.format, data.value, f
      )
    )
    conditions(data.comparator)
  }

  def toCurdate(data: FilterEntity): String = {
    String.format("%s = CURDATE()", data.field)
  }

  def find(allinId: Int, filterId: Int): Map[Int, List[Map[String, String]]] = {
    val automaticRepository: AutomaticRepository = new AutomaticRepository
    val data = automaticRepository.setAllinId(allinId).setFilterId(filterId).findFilters

    var mapGroups: Map[Int, List[Map[String, String]]] = Map.empty[Int, List[Map[String, String]]]
    var groups: Map[String, String] = Map.empty[String, String]
    var listGroups: List[Map[String, String]] = List.empty[Map[String, String]]

    data.foreach(row => {
      row.comparator = row.comparator.toLowerCase
      groups += (
        "condition" -> this.filters(row.comparator).apply(row),
        "operator" -> row.operator,
        "groupOperator" -> row.groupOperator
      )
      listGroups :+= groups
      mapGroups += (row.groupId -> listGroups)
      listGroups = List.empty[Map[String, String]]
    })
    mapGroups
  }

  def filter(queue: QueueEntity): String = {
    val groups: Map[Int, List[Map[String, String]]] = this.find(queue.rule.allinId, queue.rule.filterId)
    var filter: String = ""

    groups.foreach(group => {
      var condition = ""
      var groupOperator = ""
      var last: Int = group._2.size - 1
      var index: Int = 0

      group._2.foreach(item => {
        var operator = if (index < last) item.get("operator").get else ""
        condition = condition.concat(String.format("%s %s", item.get("condition").get, operator))
        groupOperator = String.format(" %s ", item.get("groupOperator").get)
        index += index + 1
      })
      filter = filter.concat(if (condition != "") String.format("(%s)", condition.trim) else "")

      breakable {
        if (groupOperator.trim == "") {
          return filter
        }
      }
      filter += filter.concat(groupOperator)

    })
    filter

  }
}
