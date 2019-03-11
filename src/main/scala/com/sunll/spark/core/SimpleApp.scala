package com.sunll.spark.core

import org.apache.spark.sql.SparkSession

/**
  * desc
  *
  * @author sunliangliang 2018-12-14
  * @version 1.0
  */
object SimpleApp {

  def main(args: Array[String]): Unit = {
    val logFile = "D:\\soft\\spark-2.4.0-bin-hadoop2.6\\README.md"
    val spark = SparkSession.builder().appName("SimpleApp").master("local[2]").getOrCreate()
    val logData = spark.read.textFile(logFile).persist()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(numAs)
    println(numBs)
    spark.stop()
  }
}
