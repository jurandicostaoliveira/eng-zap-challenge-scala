package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.{QueueEntity, StockEntity}
import br.com.btg360.repositories.ReferenceListRepository
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

import scala.collection.Map

class ReferenceListService extends Service {

  private val repository = new ReferenceListRepository()

  /**
    * @param QueueEntity queue
    * @return Boolean
    */
  def exists(listId: Int): Boolean = {
    try {
      val data = this.repository.listId(listId).findSettings
      if (data("isFiled") != 0 || data("isExcluded") != 0) {
        return false
      }

      true
    } catch {
      case e: Exception => println(e.printStackTrace())
        false
    }
  }

  /**
    * Places the data referring to the user coming from the reference list
    *
    * @param QueueEntity queue
    * @param RDD         data
    * @return RDD
    */
  def add(queue: QueueEntity, data: RDD[(String, StockEntity)]): RDD[(String, StockEntity)] = {
    try {
      if (!this.exists(queue.rule.referenceListId)) {
        return data
      }

      val list: RDD[(String, Row)] = this.repository
        .allinId(queue.rule.allinId)
        .listId(queue.rule.referenceListId)
        .findAll

      val columns = this.repository.describe

      data.leftOuterJoin(list).map(row => {
        var item = row._2._1
        if (row._2._2.isDefined) {
          item = new StockEntity(
            products = item.products,
            recommendations = item.recommendations,
            references = row._2._2.get.getValuesMap(columns)
          )
        }
        (row._1, item)
      })
    } catch {
      case e: Exception => println(e.printStackTrace)
        data
    }
  }

}
