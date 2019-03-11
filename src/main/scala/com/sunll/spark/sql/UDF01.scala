package com.sunll.spark.sql

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._

/**
  * 弱类型udf_聚合函数
  *
  * @author sunliangliang
  * @version 1.0
  */
object UDF01 extends UserDefinedAggregateFunction{
  override def inputSchema: StructType = StructType(StructField("inputCol", LongType) :: Nil)

  override def bufferSchema: StructType = StructType(StructField("sum", LongType) :: StructField("count", LongType) :: Nil)

  override def dataType: DataType = DoubleType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {buffer(0) = 0L; buffer(1) = 0L}

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if(!input.isNullAt(0)){
      buffer(0) = buffer.getLong(0) + input.getLong(0)
      buffer(1) = buffer.getLong(1) + 1
    }
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  override def evaluate(buffer: Row): Any = buffer.getLong(0).toDouble / buffer.getLong(1).toDouble

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").appName("111").getOrCreate()
    spark.udf.register("myaverage",UDF01)
    val df = spark.read.json("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\employees.json")
    df.createOrReplaceTempView("employes")
    spark.sql("select myaverage(salary) as ave from employes").show()
    spark.stop()
  }
}
