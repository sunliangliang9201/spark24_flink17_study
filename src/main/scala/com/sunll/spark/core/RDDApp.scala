package com.sunll.spark.core

import org.apache.hadoop.io.{IntWritable, NullWritable, Text}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * transformastions 和 actions的翻译在文本文件里（配置）resources
  *
  * @author sunliangliang 2018-12-14
  * @version 1.0
  */
object RDDApp {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("RDDApp").setMaster("local[*]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\README.md").persist()
    println(lines.getClass)
    //val totalLength = lines.map(line => line.length)
    //println(totalLength.getNumPartitions)
//    val a = sc.parallelize( 1 to 20 , 3 )
//    val b = a.sample( true , 0.8 , 0 )
//    val c = a.sample( false , 0.8 , 0 )
//    println( "RDD a : " + a.collect().mkString( " , " ) )
//    println( "RDD b : " + b.collect().mkString( " , " ) )
//    println( "RDD c : " + c.collect().mkString( " , " ) )

    //totalLength.mapPartitionsWithIndex((index,y) => func1(index, y))
    //val res = totalLength.reduce((a, b) => a+b)
    //println(res)
    //val totalLength2 = lines.mapPartitions(func(_))
//    val totalLenth2 = lines.flatMap(line => line.split(" "))
//    val res2 = totalLenth2
//    println(res2.collect())
//    val a = sc.parallelize( 1 to 20, 2)
//    val b = sc.parallelize(10 to 30, 2)
//    println(a.collect().mkString(","))
//    println(b.collect().mkString(","))
//    val c = a.union(b).distinct()
//    println(c.collect().mkString(","))
//    val a = lines.flatMap(line => line.split(" ")).map(word => (word, 1))
//    val b = a.reduceByKey((a,b) => a+b)
//    println(b.collect().mkString(","))
      val a = sc.parallelize(List((1,"a"),(2,"b"),(1,"c"),(1,"a")))
//    val c = sc.parallelize(List((2,"a"),(2,"f"),(3,"c")))
//    val b = a.leftOuterJoin(c)
//    println(b.collect().mkString(","))
    //val a = sc.parallelize(List((1,"a"),(2,"b"),(1,"c")))
//    val c = sc.parallelize(List((2,"a"),(2,"f"),(3,"c")))
//    val b = a.cartesian(c)
//    val d = a.pipe("sort -n")
//    println(a.collect().mkString(","))
    //println(b.collect().mkString(","))
      //val a = sc.parallelize(1 to 20)
    //println(a.getNumPartitions)
//    println(a.top(1).mkString(""))
//    val res = a.takeOrdered(1)
//    println(res.mkString(""))
    //a.saveAsTextFile("d:\\11\\spark.txt")
    //val a = sc.textFile("d:\\\\11\\\\spark.txt")
    //println(a.collect().mkString(","))
    //a.saveAsSequenceFile("d:\\\\\\\\12\\\\\\\\spark.txt")
    //a.saveAsObjectFile("d:\\11\\spark.txt")
    //val a = sc.sequenceFile[Int, String]("d:\\12\\spark.txt\\part-00000")
    //a.foreachPartition(println(_))
    //println(a.collect().toList)
    //val b = sc.objectFile("d:\\11\\spark.txt")
    //println(b.take(1))
    val res = a.countByValue()
    println(res)
    sc.stop()
  }

  def func(y: Iterator[String]): Iterator[String] ={
    for (i <- y){
      println(i)
    }
    y
  }
  def func1(index: Int, y: Iterator[Int]): Iterator[Any] ={
    var res = List[Int]()
    var j =""
    for (i <- y){
      j += i
    }
    res.::(j).iterator
  }
}
