package com.sunll.flink.batch

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.api.scala._
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object BatchExample {

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    env.setNumberOfExecutionRetries(3)
    env.getConfig.setExecutionRetryDelay(5000)
    val text: DataSet[String] = env.fromElements("i love beijing","i love china", "beijing is the capital of china")
    val counts = text.flatMap(_.toLowerCase.split("\\W+")).filter(_.nonEmpty).map((_, 1)).groupBy(0).sum(1)
    counts.print()
  }
}
