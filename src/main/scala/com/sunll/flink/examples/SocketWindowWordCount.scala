package com.sunll.flink.examples

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time

/**
  *stream processing
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object SocketWindowWordCount {

  def main(args: Array[String]): Unit = {
//    val port: Int = try{
//      ParameterTool.fromArgs(args).getInt("port")
//    }catch {
//      case e: Exception => {
//        System.err.print("No port specified. Please run 'SocketWindowWordCount --port <port>'")
//        return
//      }
//    }
    val port = 9898
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setBufferTimeout(1000)
    //env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime)
    val text: DataStream[String] = env.socketTextStream("150.138.123.221", port, '\n')
    val windowCount: DataStream[WordWithCount] = text
                                                    .flatMap(_.split("\\s"))
                                                    .map(WordWithCount(_, 1))
                                                    .keyBy("word")
                                                    .timeWindow(Time.seconds(5), Time.seconds(1))
                                                      .sum(1)
    windowCount.print().setParallelism(1)
    env.execute("Socket Window WordCount")
  }
}
case class WordWithCount(word: String, count: Int)