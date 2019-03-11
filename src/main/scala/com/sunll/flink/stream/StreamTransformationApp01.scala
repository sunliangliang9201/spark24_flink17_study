package com.sunll.flink.stream

import org.apache.flink.api.common.functions.Partitioner
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks
import org.apache.flink.streaming.api.watermark.Watermark
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object StreamTransformationApp01 {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val input: DataStream[(String, Long)] = env.fromElements(
      ("a", 100L),
      ("b", 200L),
      ("c", 300L),
      ("a", 400L))
    //val res = input.keyBy(0).reduce((x, y) => (x._1, x._2 +y._2))
    //val res = input.keyBy(0).fold(100L)((x,y) => x + y._2)
//    val res: DataStream[(String, Long)] = input.keyBy(0).minBy(1)
//    val res2: DataStream[(String, Long)] = input.keyBy(0).min(1)
//    res.print()
//    res2.print()
    //val res = input.keyBy(0).window(TumblingEventTimeWindows.of(Time.seconds(5)))
    val input2: DataStream[Int] = env.fromElements(
      100,
      201,
      300,
      401)

//    val res = input.union(input2)
//    res.print()
//    val res = input.join(input2).where(_._1).equalTo(_._1).window(TumblingEventTimeWindows.of(Time.seconds(5)))
//    val res = input.connect(input2)
//    res.map(
//      (_: (String, Long)) => "ok",
//      (_: Long) => "no"
//    ).print()
//    val splits = input2.split((num: Int) => {
//  (num % 2) match {
//    case 0 => List("1")
//    case 1 => List("2")
//  }
//})
//    splits.print()
//    val res = input2.iterate(iteration => {
//  val a = iteration.map(_ - 10)
//  a.print()
//  (a.filter(_ > 0), a.filter(_ < 0))
//})
//    res.print()
//    val res = input.assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks[(String, Long)] {
//  var currentMaxTimestamp: Long = _
//  val maxOutOfOrderness = 5000L //5s
//
//  override def extractTimestamp(element: (String, Long), previousElementTimestamp: Long): Long = {
//    val timestamp = element._2//获取创建时间，这里的数据并没有时间，所以需要假定一个时间戳字段
//    currentMaxTimestamp = Math.max(currentMaxTimestamp, timestamp)
//    timestamp
//  }
//
//  override def getCurrentWatermark: Watermark = {
//    new Watermark(currentMaxTimestamp - maxOutOfOrderness)
//}
//})
//    val res = input.partitionCustom(new Partitioner[String] {
//  override def partition(k: String, i: Int): Int = {
//    return 0
//  }
//},0)
//    res.print()
    //input.broadcast()
    val res = input.map(_._2).startNewChain().map(x => x).disableChaining().map(x => x)
    res.print()
    env.execute()

  }
}
