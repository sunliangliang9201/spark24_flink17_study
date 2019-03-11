package com.sunll.flink.examples

import org.apache.flink.api.scala.ExecutionEnvironment

/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object PageRank {

  case class Link(sourceId: Long, targetId: Long)
  case class Page(pageId: Long, rank: Double)
  case class AdjacencyList(sourceId: Long, targetIds: Array[Long])

  def main(args: Array[String]): Unit = {
    val env = ExecutionEnvironment.getExecutionEnvironment

  }
}
