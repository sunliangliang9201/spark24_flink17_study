package com.sunll.spark.sql

import org.apache.spark.sql.{Encoder, Encoders, SparkSession}
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.expressions.Aggregator


/**
  * 强类型UDF——聚合函数
  *
  * @author sunliangliang 2018-09-28 https://github.com/sunliangliang9201/tvtest_streaming
  * @version 1.0
  */
case class Employee(name: String, salary: Long)
case class Average(var sum: Long, var count: Long)

object UDF02 extends Aggregator[Employee, Average, Double]{
  override def zero: Average = Average(0L, 0L)

  override def reduce(b: Average, a: Employee): Average = {
    b.sum += a.salary
    b.count += 1
    b
  }

  override def merge(b1: Average, b2: Average): Average = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  override def finish(reduction: Average): Double = reduction.sum.toDouble / reduction.count.toDouble

  override def bufferEncoder: Encoder[Average] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName("1").master("local[*]").getOrCreate()
    import spark.implicits._
    val df = spark.read.json("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\employees.json").as[Employee]
    df.show()
    val averageSalary  = UDF02.toColumn.name("average_salary")
    df.select(averageSalary).show
    spark.stop()
  }
}
