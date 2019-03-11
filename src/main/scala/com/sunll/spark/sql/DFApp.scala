package com.sunll.spark.sql

import com.alibaba.fastjson.JSON
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * desc
  *
  * @author sunliangliang
  * @version 1.0
  */
object DFApp {

  def main(args: Array[String]): Unit = {
//    val arr = Seq("tome:10", "bob:14", "hurry:9")
//    val spark = SparkSession.builder().appName("SparkSQL").master("local[*]").getOrCreate()
//    val rdd1 = spark.sparkContext.parallelize(arr)
//    val res = rdd1.map(x => {
//      val arr = x.split(":")
//      val name = arr(0)
//      val age = arr(1).toInt
//      val p = new People(name, age)
//      p
//    }).map(x => {
//      println(x.name)
//      JSON.toJSONString(x, SerializerFeature.WriteMapNullValue)
//    })
//    res.foreach(println(_))
//    spark.stop()
    val spark = SparkSession.builder().master("local[*]").appName("1").getOrCreate()
//    import spark.implicits._
//    val rdd = spark.read.textFile("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\people.txt")
//    val df = rdd.map(x => {
//      val p = new People(x.split(",")(0).trim, x.split(",")(1).trim.toInt)
//      p
//    }).toDF()
////    df.show()
////    df.printSchema()
////    df.select($"name", $"age" + 1).show()
//    df.createOrReplaceTempView("people")
//    spark.sql("select * from people").show()
//    df.createOrReplaceGlobalTempView("people")
//    spark.newSession().sql("select * from global_temp.people").show()
    //import spark.implicits._
    //已知case class来对应输入的数据
//    val df = spark.read.textFile("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\people.txt")
//    val df2 = df.map(_.split(",")).map(attr => People(attr(0), attr(1).trim.toInt)).toDF()
//    df2.show()
//    df2.createOrReplaceTempView("people")
//    val tee = spark.sql("SELECT name, age FROM people WHERE age BETWEEN 13 AND 19")
//    tee.show()
//    val tee2 = tee.map(t => "Name:" + t.getAs(0))
//    tee2.show()
    //无法提前确定case class，可以根据配置文件配置字段序列来动态定义schema

    val rdd = spark.sparkContext.textFile("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\people.txt")
    println(rdd.getClass)
    import spark.implicits._
    val schemaString = "name, age"
    val fileds = schemaString.split(",").map(filedName => StructField(filedName, StringType,nullable = true))
    val schema = StructType(fileds)
    val rdd2 = rdd.map(_.split(",")).map(attr => Row(attr(0), attr(1).trim))
    val peopleDF = spark.createDataFrame(rdd2, schema)
    peopleDF.createOrReplaceTempView("people")

    spark.stop()
  }

  case class People(name: String, age: Int)
}
