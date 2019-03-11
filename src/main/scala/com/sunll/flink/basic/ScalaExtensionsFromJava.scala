package com.sunll.flink.basic

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.extensions._
import org.apache.flink.api.scala.extensions._
/**
  * 匿名模式匹配！！！，条件是import上面这俩，以及方法后面添加With
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object ScalaExtensionsFromJava {

  import org.apache.flink.api.scala.extensions._
  case class Point(x: Double, y: Double)
  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment
    val ds = env.fromElements(Point(1, 2), Point(3, 4), Point(5, 6))
    val res = ds.filterWith {
      case Point(x, _) => x > 1
    }.reduceWith {
      case (Point(x1, y1), (Point(x2, y2))) => Point(x1 + y1, x2 + y2)
    }.mapWith {
      case Point(x, y) => (x, y)
    }.flatMapWith {
      case (x, y) => Seq("x" -> x, "y" -> y)
    }.groupingBy {
      case (id, value) => id
    }
    res.first(1).print()
  }
}
