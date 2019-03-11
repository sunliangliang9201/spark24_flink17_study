package com.sunll.spark.sql

import org.apache.spark.sql.SparkSession

/**
  * desc
  *
  * @author sunliangliang
  * @version 1.0
  */
object DSApp {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("2").master("local[*]").getOrCreate()
    import spark.implicits._
//    val DS1 = List(("a", 1), ("b", 2), ("c", 3)).toDS
//    val DS2 = Seq(Person("Andy", 32)).toDS
//    DS1.show()
//    DS2.show()
    val ds1 = spark.read.json("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\people.json").as[Person]
    ds1.show()
    spark.stop()
  }
  case class Person(name: String, age: Long)
}
