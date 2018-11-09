package br.com.btg360.services

import br.com.btg360.application.Service
import br.com.btg360.entities.{ItemEntity, QueueEntity}
import br.com.btg360.repositories.ReferenceListRepository
import org.apache.spark.rdd.RDD

import scala.collection.immutable.Map

class ReferenceListService extends Service {

  private val repository = new ReferenceListRepository()

  /**
    * @param QueueEntity queue
    * @return Boolean
    */
  private def exists(listId: Int): Boolean = {
    try {
      val data = this.repository.listId(listId).findSettings
      if (data == null || !data.next()) {
        return false
      }

      if (data.getInt("fl_arquivado") != 0 || data.getInt("fl_excluir") != 0) {
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
  def add(queue: QueueEntity, data: RDD[(String, ItemEntity)]): RDD[(String, ItemEntity)] = {
    try {
      if (!this.exists(queue.rule.referenceListId)) {
        return data
      }

      val list: RDD[(String, Map[String, String])] = this.repository.allinId(queue.rule.allinId)
        .listId(queue.rule.referenceListId).findAll.map(row => (row("nm_email"), row.toMap))

      val join = data.leftOuterJoin(list).map(row => {
        val entity = row._2._1
        if (row._2._2.isDefined) {
          entity.addReferences(row._2._2.get)
        }
        (row._1, entity)
      })

      join
    } catch {
      case e: Exception => println(e.printStackTrace)
        data
    }
  }

}
