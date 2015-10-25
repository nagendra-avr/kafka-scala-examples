import java.util.Properties

import kafka.producer.{KeyedMessage, ProducerConfig, Producer}

/**
 * Created by Nagendra on 10/25/15.
 */

/**
 * Simple progrma to demonstrate implementation of kafka producer application to send data
 *
 * @param brokerList - Value for kafka's broker list property metadata.broker.list
 * @param producerConfig - More producer configurations
 * @param defaultTopic - The default topic to Kafka; the default topic is used as a fallback when you not provide a specific topic when calling.
 * @param producer - An existing [[kafka.producer.Producer]] instance to use for sending data to Kafka. Primarily used
 * for testing. If `producer` is set, then we ignore the `brokerList` and `producerConfig` parameters.
 */
case class SimpleKafkaProducer(brokerList: String, producerConfig: Properties = new Properties(),defaultTopic : Option[String],producer: Option[Producer[String, String]] = None) {

  type Key = String
  type Val = String

  require(brokerList == null || !brokerList.isEmpty, "Must set broker list")

  private val p = producer getOrElse {
    val effectiveConfig = {
      val config = new Properties
      config.put("metadata.broker.list", "localhost:9092")
      config.put("request.required.acks", "1")
      config.put("serializer.class", "kafka.serializer.StringEncoder")
      config
    }
    new Producer[String, String](new ProducerConfig(effectiveConfig))
  }

  val config = p.config

  private def toMessage(value : Val, key : Option[Key] = None, topic: Option[String]=None): KeyedMessage[Key, Val] = {
    val t = topic.getOrElse(defaultTopic.getOrElse(throw new IllegalArgumentException("Must provide topic or default topic")))
    require(!t.isEmpty, "Topic must not be empty")
    key match {
      case Some(k) => new KeyedMessage(t,k,value)
      case _ => new KeyedMessage(t,value)
    }
  }

  def send(key: Key, value: Val, topic: Option[String]=None) {
    p.send(toMessage(value,Option(key),topic))
  }

  def send(value: Val, topic: Option[String]) {
    send(null, value, topic)
  }

  def send(value: Val, topic: String) {
    send(null, value, Option(topic))
  }

  def send(value: Val) {
    send(null, value, None)
  }
  def shutdown(): Unit = p.close()

}
