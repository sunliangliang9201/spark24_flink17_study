package com.sunll.flink.stream

import java.util.Properties

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.{JSONKeyValueDeserializationSchema, SimpleStringSchema}
/**
  * 会介绍几个常用的流式connector
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object StreamConnectorApp02 {

  def main(args: Array[String]): Unit = {
    kafkaConnector()
  }

  def kafkaConnector(): Unit ={
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val prop: Properties = new Properties()
    prop.setProperty("bootstrap.servers", "103.26.158.194:9092, 103.26.158.195:9092")
    prop.setProperty("group.id", "test")
    val kafkaConsumer = new FlinkKafkaConsumer09[String]("bf.bftv.tv_real_time", new SimpleStringSchema(), prop)
    kafkaConsumer.setStartFromLatest()
    kafkaConsumer.setStartFromGroupOffsets()
    val kafkaStream: DataStream[(String)] = env.addSource(kafkaConsumer)
    kafkaStream.print()

    env.execute()
  }
}
