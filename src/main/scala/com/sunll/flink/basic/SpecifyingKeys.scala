package com.sunll.flink.basic

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.datastream.DataStreamUtils
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object SpecifyingKeys {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    val input: DataStream[(Int, String, Long)] = env.fromElements(
      (1, "a", 100),
      (2, "b", 200),
      (2, "c", 300),
      (3, "a", 400))
    //第一种：直接根据字段位置指定，可以指定多个key
    var keyedStream = input.keyBy(1)
    keyedStream = input.keyBy(0,1)

    //第二种：用case class的方式
    val input2: DataStream[C1] = env.fromElements(
      C1(1, "a", 100),
      C1(2, "b", 200),
      C1(2, "c", 300),
      C1(3, "a", 400))
    val keyedStream2 = input2.keyBy("id")

    //第三种：selector function，说白了就是前两种方式加点逻辑！
    val input3: DataStream[C1] = env.fromElements(
      C1(1, "a", 100),
      C1(2, "b", 200),
      C1(2, "c", 300),
      C1(3, "a", 400))
    val keyedStream3 = input3.keyBy(_.id)

  }
  case class C1(id: Int, name: String, price: Long)
}
