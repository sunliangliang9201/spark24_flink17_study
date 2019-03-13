package com.sunll.flink.stream

import java.util.Properties

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
//import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.{JSONKeyValueDeserializationSchema, SimpleStringSchema}
/**
  * 会介绍几个常用的流式connector，这里只有kafka的，其他的没有- -
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
    env.enableCheckpointing(5000)
    val prop: Properties = new Properties()
    prop.setProperty("bootstrap.servers", "103.26.158.182:9092, 103.26.158.183:9092")
    prop.setProperty("group.id", "test")
//    val kafkaConsumer = new FlinkKafkaConsumer09[String]("bf.bftv.tv_real_time", new SimpleStringSchema(), prop)
//    kafkaConsumer.setStartFromLatest()
//    kafkaConsumer.setStartFromGroupOffsets()
//    val kafkaStream: DataStream[(String)] = env.addSource(kafkaConsumer)
//    kafkaStream.print()
    //看看新版本的message有什么不同
//    val kafkaConsumer2 = new FlinkKafkaConsumer[String]("bf.bftv.tv_real_time", new SimpleStringSchema(), prop)
//    kafkaConsumer2.setStartFromLatest()
//    kafkaConsumer2.setStartFromGroupOffsets()
//    val kafkaStream = env.addSource(kafkaConsumer2)
//    kafkaStream.print()
    env.execute()
  }
}
