package com.sunll.flink.batch

import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.api.common.operators.Order
import org.apache.flink.api.common.operators.base.JoinOperatorBase.JoinHint
import org.apache.flink.api.java.aggregation.Aggregations
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration

import scala.collection.JavaConverters._
import scala.collection.mutable


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
    val input2 = env.fromElements(("a", 200), ("b", 100), ("c",300), ("d", 500)).setParallelism(4)
//    input.coGroup(input2).where(0).equalTo(0).map(_._2).print()
    //input.cross(input2).print()
    //input.union(input2).print()
//    input.first(2).print()
//    input.groupBy(0).first(2).print()
//    println("-------------------")
//    input.groupBy(0).sortGroup(1, Order.DESCENDING).first(2).print()
    //input2.minBy(1, 0).print()
//    input2.sortPartition(1, Order.ASCENDING).print()//仅仅是分区内的排序
//    input2.setParallelism(1).sortPartition(1, Order.ASCENDING).print()//全部元素排序
//    input2.sortPartition(1, Order.ASCENDING).writeAsText("E:\\test\\1.txt\\")
//    env.execute()
    //input.map(x => ("foo", x._2, x._1)).withForwardedFields("_1->_3; _2").print()

    //广播变量
    input.map(new RichMapFunction[(String, Int), (String, Int, Int)](){
      var bc: Traversable[(String, Int)]= null
      var res = 0
      override def open(parameters: Configuration): Unit = {
        bc = getRuntimeContext.getBroadcastVariable[(String, Int)]("bc_input2").asScala
        for (i <- bc){
          res += i._2
        }
      }
      override def map(in: (String, Int)): (String, Int, Int) = {
        (in._1, in._2, res)
      }
    }).withBroadcastSet(input2, "bc_input2").print()

  }
}
