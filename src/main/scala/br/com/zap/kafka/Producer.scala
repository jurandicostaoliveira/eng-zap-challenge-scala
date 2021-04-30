package br.com.zap.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties

class Producer extends Serializable {

  val properties: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props
  }

  def writeMessage(topic: String, message: String): Unit = {
    try {
      val producer = new KafkaProducer[String, String](properties)
      val record = new ProducerRecord[String, String](topic, message)
      producer.send(record)
      producer.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
