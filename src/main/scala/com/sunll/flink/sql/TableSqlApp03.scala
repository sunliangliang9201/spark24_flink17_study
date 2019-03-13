package com.sunll.flink.sql

import java.time.ZoneId
import java.util
import java.util.TimeZone

import org.apache.flink.api.common.state.StateDescriptor.Type
import org.apache.flink.api.common.time.Time
import org.apache.flink.api.common.typeinfo.Types
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.table.api.scala._
import org.apache.flink.api.scala._
import org.apache.flink.table.descriptors._
import org.apache.flink.table._
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object TableSqlApp03 {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.enableCheckpointing(5000)
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val tableConfig = tableEnv.getConfig
    tableConfig.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
    val queryConfig = tableEnv.queryConfig
    queryConfig.withIdleStateRetentionTime(Time.hours(12), Time.hours(24))
    //这种方式不好使哎
    tableEnv
      .connect(
      new Kafka()
        .version("0.11")
        .topic("bf.bftv.tv_real_time")
        .startFromLatest()
          .property("bootstrap.servers", "103.26.158.182:9092, 103.26.158.183:9092")
    )
        //缺format
      .withSchema(
        new Schema()
          .field("message", Types.STRING)
          //.field("timestamp", Types.SQL_TIMESTAMP).rowtime(new Rowtime().timestampsFromSource())
      )
      .inAppendMode()
      .registerTableSource("kafkaTable")

    tableEnv.scan("kafkaTable").select("message").toAppendStream[String](queryConfig).print()
    env.execute()
  }
}
