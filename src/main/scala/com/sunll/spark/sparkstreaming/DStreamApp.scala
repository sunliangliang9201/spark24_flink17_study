package com.sunll.spark.sparkstreaming

import org.apache.hadoop.io.{IntWritable, LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.spark.{HashPartitioner, SparkConf}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.execution.datasources.csv.CSVFileFormat
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * desc
  *
  * @author sunliangliang
  * @version 1.0
  */
object DStreamApp {
  val updateFunction = (iter: Iterator[(String, Seq[Int], Option[Int])]) => {
    iter.flatMap(it => Some(it._2.sum + it._3.getOrElse(0)).map(x => (it._1, x)))
  }
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("a")
    conf.set("spark.executor.extraJavaOptions", "-verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps")
    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("e:\\test")
    val lines = ssc.socketTextStream("150.138.123.194", 9999)
//    val words = lines.flatMap(_.split(" "))
//    val pairs = words.map(word => (word, 1))
//    val wordCounts = pairs.reduceByKey(_ + _)
//    wordCounts.print()
//    val lines = ssc.textFileStream("e:\\test\\")
//    val words = lines.flatMap(_.split(" "))
//    val pairs = words.map(word => (word, 1))
//    val wordCounts = pairs.reduceByKey(_ + _)
//    wordCounts.print()
//    val res = lines.flatMap(_.split(" ")).map((_,1)).updateStateByKey(updateFunction, new HashPartitioner(ssc.sparkContext.defaultParallelism),true)
//    res.print()
    val pairs = lines.flatMap(_.split(" ")).map((_, 1))
    val res = pairs.reduceByKeyAndWindow((a: Int, b: Int) => (a+b), Seconds(10), Seconds(5))
//    val res = pairs.window(Seconds(5))
//    res.print()
//    res.saveAsTextFiles("e:\\test\\a")
//    res.saveAsObjectFiles("e:\\test\\a")
    res.print()
    ssc.start() // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}