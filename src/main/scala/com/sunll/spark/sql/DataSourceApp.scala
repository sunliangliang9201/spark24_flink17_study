package com.sunll.spark.sql

import java.util.Properties

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * desc
  *
  * @author sunliangliang
  * @version 1.0
  */
object DataSourceApp {

  def main(args: Array[String]): Unit = {
    //一些各种类型文件的加载、保存、转换的操作
    val spark = SparkSession.builder().appName("1").master("local[2]").getOrCreate()
    //val parquetDF = spark.read.format("parquet").load("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\users.parquet")
//    parquetDF.createOrReplaceTempView("users")
//    spark.sql("select name, favorite_numbers from users where name='Alyssa'").write.save("e:\\user.parquet")
//    val user1 = spark.read.load("e:\\user.parquet")
//    user1.show()
//    parquetDF.select("name", "favorite_numbers").write.format("json").save("e:\\user.json")
//    val csvDF = spark.read.format("csv").option("sep", ";")
//                                          .option("inferschema", true)
//                                            .option("header", "true")
//                                             .load("D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\people.csv")
//
//    csvDF.show()
//    val sqlDicDF = spark.sql("select * from json.`D:\\\\soft\\\\spark-2.4.0-bin-hadoop2.6\\\\examples\\\\src\\\\main\\\\resources\\\\people.json`")
//    sqlDicDF.show()
//    sqlDicDF.write.mode(SaveMode.Overwrite).format("json").save("e:\\11")
    //parquetDF.write.partitionBy("favorite_color").mode(SaveMode.Overwrite).format("json").saveAsTable("user1")
    //parquetDF.write.bucketBy(10, "name").format("json").saveAsTable("user2")
//    import spark.implicits._
//    val field1DF = spark.sparkContext.makeRDD(1 to 5).map(i => (i, i)).toDF("value", "f1")
//    field1DF.write.format("json").save("e:\\table\\key=1")
//    val field2DF = spark.sparkContext.makeRDD(10 to 20).map(i => (i, i)).toDF("value", "f2")
//    field2DF.write.format("json").save("e:\\table\\key=2")
//    val mergedDF = spark.read.option("mergeSchema", "true").format("json").load("e:\\table")
//    mergedDF.show()
//    mergedDF.printSchema()
//    val connectionProperties = new Properties()
//    connectionProperties.put("user", "dtadmin")
//    connectionProperties.put("password", "Dtadmin123!@#")
//    val jdbcDF2 = spark.read
//      .jdbc("jdbc:mysql://103.26.158.77:3306", "azkaban3.executors", connectionProperties)
//    jdbcDF2.show(2)
//    val spark = SparkSession.builder().master("local[*]").appName("1").config("spark.sql.warehourse.dir", "spark-warehouse").enableHiveSupport().getOrCreate()
//    val userDF = spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING) using hive")
//    spark.sql("sql(\"LOAD DATA LOCAL INPATH 'D:\\soft\\spark-2.4.0-bin-hadoop2.6\\examples\\src\\main\\resources\\kv1.txt' INTO TABLE src\")")
//    spark.sql("SELECT * FROM src").show()
//    val avroDF = spark.read.format("avro").load("D:\\\\soft\\\\spark-2.4.0-bin-hadoop2.6\\\\examples\\\\src\\\\main\\\\resources\\\\users.avro")
//    avroDF.show()
    val pro = new Properties()
    pro.setProperty("user", "huogang")
    pro.setProperty("passwd", "KB@:4?nLj4v(by+-%h2")
    val hiveDF = spark.read.jdbc("jdbc:hive2//103.26.158.33:11112", "tv.tv_click_new_org", pro)
    hiveDF.show(1)
    spark.stop()
  }
}
