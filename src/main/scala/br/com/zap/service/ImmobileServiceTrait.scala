package br.com.zap.service

import br.com.zap.domain.Immobile
import br.com.zap.kafka.{Consumer, Producer}
import br.com.zap.repository.ImmobileRepository
import org.apache.spark.rdd.RDD

trait ImmobileServiceTrait extends Serializable {

  /**
   *
   * @return
   */
  protected def getTopic: String

  /**
   *
   * @param data
   * @return
   */
  protected def dataFilter(data: RDD[Immobile]): RDD[Immobile]

  /**
   *
   * @param data
   * @return
   */
  protected def dataMap(data: RDD[Immobile]): RDD[Immobile]

  /**
   *
   */
  def producer: Unit = {
    try {
      dataMap(
        dataFilter(new ImmobileRepository().all())
      ).foreach(row => {
        new Producer().writeMessage(getTopic, row.toString)
      })
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

  /**
   *
   */
  def consumer: Unit = {
    try {
      new Consumer().printMessage(getTopic)
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
