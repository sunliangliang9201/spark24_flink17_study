package com.sunll.flink.batch

import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.common.operators.base.JoinOperatorBase.JoinHint
import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object BatchTransformationsApp01 {

  def main(args: Array[String]): Unit = {
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    val input = env.fromElements(("a", 100), ("b", 200), ("c",300), ("a", 500)).setParallelism(2)
//    input.mapPartition(x => x.map((_, "ok"))).print()
    //input.groupBy(0).reduce((x, y) => (x._1, x._2 + y._2)).print()
//    input.groupBy(0).reduceGroup(x => x.sum).print()
//    input.aggregate(Aggregations.SUM, 1).aggregate(Aggregations.MIN,1).print()
//    input.sum(1).print()
//    input.distinct().print()
//    input.distinct(0).print()
    //input.join(input, JoinHint.BROADCAST_HASH_FIRST).where(0).equalTo(0).print()
    val input2 = env.fromElements(("a", 200), ("b", 100), ("c",300), ("d", 500)).setParallelism(2)
//    input.coGroup(input2).where(0).equalTo(0).map(_._2).print()
    //input.cross(input2).print()
    //input.union(input2).print()
//    input.first(2).print()
//    input.groupBy(0).first(2).print()
//    println("-------------------")
//    input.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print()
    input2.minBy(1, 0).print()
  }
}
