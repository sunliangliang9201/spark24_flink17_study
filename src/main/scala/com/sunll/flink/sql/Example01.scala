package com.sunll.flink.sql

import java.sql.Timestamp
import java.util.TimeZone

import org.apache.flink.api.common.time.Time
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.TableEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.table.api.scala._
import org.apache.flink.table.descriptors.Rowtime
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object Example01 {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.enableCheckpointing(5000)
    val tableEnv = TableEnvironment.getTableEnvironment(env)
    val tableConfig = tableEnv.getConfig
    tableConfig.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
    val queryConfig = tableEnv.queryConfig
    queryConfig.withIdleStateRetentionTime(Time.hours(12), Time.hours(24))
    // read a DataStream from an external source
    val ds: DataStream[(Long, String, Int)] = env.fromElements((1000L, "a", 1), (2000L, "b", 2), (3000L, "c", 3)).assignAscendingTimestamps(_._1)
//    tableEnv.registerDataStream("t1", ds, 'a, 'b, 'c)
//    tableEnv.sqlQuery("select * from t1").toAppendStream[(Long, String ,Int)](queryConfig).print()

    // register the DataStream under the name "Orders"
    //经过多次试验，终于明白了processingtime和eventtime到底是怎么与字段与表字段对应的了！！！
    //第一：processing time字段时程序给的！！！只能追加到后面，不能占据其他字段的位置
    //第二：如果想用eventtime的话，数据源必须带有timestamp这个东西，可以不体现在字段中，但是一定要有！！如果没有用用assigner来抽取
    //第三：调用时间戳的方式如下！！！
    tableEnv.registerDataStream("Orders", ds, 'user, 'product, 'amount, 'proctime.proctime, 'rowtime.rowtime)
    // compute SUM(amount) per day (in event-time)
    tableEnv.sqlQuery("select * from Orders").toAppendStream[(Long, String, Int, Timestamp, Timestamp)](queryConfig).print()

    ////下面这几个例子作为之后实现tv展示需求的法宝！！当然由于上面数据不合格所以无法看下面这几个query的结果
    val result1 = tableEnv.sqlQuery(
      """
        |SELECT
        |  user,
        |  TUMBLE_START(rowtime, INTERVAL '2' second) as wStart,
        |  SUM(amount)
        | FROM Orders
        | GROUP BY TUMBLE(rowtime, INTERVAL '2' second), user
      """.stripMargin)
    result1.toAppendStream[(Long, Timestamp, Int)](queryConfig).print()
     //compute SUM(amount) per day (in processing-time)
    val result2 = tableEnv.sqlQuery(
      "SELECT user, SUM(amount) FROM Orders GROUP BY TUMBLE(proctime, INTERVAL '1' DAY), user")

    // compute every hour the SUM(amount) of the last 24 hours in event-time
    val result3 = tableEnv.sqlQuery(
      "SELECT product, SUM(amount) FROM Orders GROUP BY HOP(rowtime, INTERVAL '1' HOUR, INTERVAL '1' DAY), product")

    // compute SUM(amount) per session with 12 hour inactivity gap (in event-time)
    val result4 = tableEnv.sqlQuery(
      """
        |SELECT
        |  user,
        |  SESSION_START(rowtime, INTERVAL '12' HOUR) AS sStart,
        |  SESSION_END(rowtime, INTERVAL '12' HOUR) AS sEnd,
        |  SUM(amount)
        | FROM Orders
        | GROUP BY SESSION(rowtime, INTERVAL '12' HOUR), user
      """.stripMargin)
    env.execute()
  }
}
