package com.sunll.flink.sql

import java.sql.Timestamp
import java.util.Properties

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer09
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala.Tumble
import org.apache.flink.table.api.scala._
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object TableSqlApp02 {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    //下面是以processing time为例，直接将系统的执行时间processing time作为时间戳
//    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
//    val stream: DataStream[(String, String)] = env.fromElements(("a","aa"), ("b", "bb"), ("c", "cc"))
//
//    // declare an additional logical field as a processing time attribute
//    val table = tableEnv.fromDataStream(stream, 'Username, 'Data, 'timestamp.proctime)
//    //val windowedTable = table.window(Tumble over 10.minutes on 'timestamp as 'timestampWindow)
//    tableEnv.registerTable("t1", table)
//    tableEnv.sqlQuery("select * from t1").toAppendStream[(String, String, Timestamp)].print()

    //下面将介绍以eventtime为例，先用assinger利用时间字段抽取eventtime作为时间戳，并用period watermarket方式添加水印
    //然后再将这个时间戳替换（不替换也ok）用来抽取时间戳的时间字段
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.enableCheckpointing(5000)
    val prop: Properties = new Properties()
    prop.setProperty("bootstrap.servers", "103.26.158.194:9092, 103.26.158.195:9092, 103.26.158.196:9092, 103.26.158.197:9092")
    prop.setProperty("group.id", "test")
    prop.setProperty("flink.partition-discovery.interval-millis", "5000");
    val kafkaConsumer = new FlinkKafkaConsumer09[String]("bf.bftv.tv_real_time", new SimpleStringSchema(), prop)
    kafkaConsumer.setStartFromLatest()
    //kafkaConsumer.setStartFromGroupOffsets()
    val kafkaStream: DataStream[(String)] = env.addSource(kafkaConsumer)
    val kafkaStreamWithT = kafkaStream.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[String] {
      val currentMaxTimeStamp: Long = 0L
      val maxOutOrderness: Long = 5000L
      override def getCurrentWatermark: Watermark = {
        new Watermark(currentMaxTimeStamp - maxOutOrderness)
      }

      override def extractTimestamp(element: String, previousElementTimestamp: Long): Long = {
//        if (null != element && "" != element){
//          val part = element.split(" ")(3).toLong
//          return if (part > currentMaxTimeStamp) System.currentTimeMillis() else currentMaxTimeStamp
//        }
        if (System.currentTimeMillis() > currentMaxTimeStamp) System.currentTimeMillis() else currentMaxTimeStamp
      }
    })
    val table2 = tableEnv.fromDataStream(kafkaStreamWithT, 'log, 'timestamp.rowtime)
//    table2.printSchema()
    // table2.select("*").toAppendStream[(String, Timestamp)].print()
    val windowedTable = table2.window(Tumble over 10.seconds on 'timestamp as 'timestampWindow)

    env.execute()
  }
}
