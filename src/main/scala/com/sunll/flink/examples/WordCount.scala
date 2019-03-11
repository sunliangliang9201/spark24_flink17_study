package com.sunll.flink.examples

import org.apache.flink.api.common.accumulators.IntCounter
import org.apache.flink.api.scala._

/**
  * batch processing
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object WordCount {

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val text: DataSet[String] = env.readTextFile("e:/ip_area_isp.txt")
    //计算一下一共多少个城市：339，我去，发现个问题，tv上报的城市最多339原来是这个缘故，感觉丢失了很多城市的数据
//    val counts = text.map(_.split("\\s")(2)).distinct().count()
//    println(counts)
    val counts: DataSet[(String, Int)] = text.flatMap(_.split("\\s")).filter(_.nonEmpty).map((_, 1)).groupBy(0).sum(1)
    counts.print()

  }
}
