package br.com.zap.kafka

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration
import java.util
import java.util.{Properties, UUID}
import scala.collection.JavaConverters._

class Consumer extends Serializable {

  val properties: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConfig.BOOTSTRAP_SERVERS)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString)
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props
  }

  def printMessage(topic: String): Unit = {
    try {
      val consumer = new KafkaConsumer[String, String](properties)
      consumer.subscribe(util.Arrays.asList(topic))

      while (true) {
        val record = consumer.poll(Duration.ofSeconds(1)).asScala
        for (data <- record.iterator) {
          println(data.value())
        }
      }
      //consumer.close()
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }

}
