package com.sunll.flink.batch

import com.sunll.spark.sql.DSApp.Person
import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.hadoop.io.{IntWritable, Text}

/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object BatchSourceApp01 {

  def main(args: Array[String]): Unit = {
    val env  = ExecutionEnvironment.getExecutionEnvironment

    // read text file from local files system
    val localLines = env.readTextFile("file:///path/to/my/textfile")

    // read text file from a HDFS running at nnHost:nnPort
    val hdfsLines = env.readTextFile("hdfs://nnHost:nnPort/path/to/my/textfile")

    // read a CSV file with three fields
    var csvInput = env.readCsvFile[(Int, String, Double)]("hdfs:///the/CSV/file")

    // read a CSV file with five fields, taking only two of them
    val csvInput1 = env.readCsvFile[(String, Double)](
      "hdfs:///the/CSV/file",
      includedFields = Array(0, 3)) // take the first and the fourth field

    // CSV input can also be used with Case Classes
    case class MyCaseClass(str: String, dbl: Double)
    val csvInput2 = env.readCsvFile[MyCaseClass](
      "hdfs:///the/CSV/file",
      includedFields = Array(0, 3)) // take the first and the fourth field

    // read a CSV file with three fields into a POJO (Person) with corresponding fields
    val csvInput3 = env.readCsvFile[Person](
      "hdfs:///the/CSV/file",
      pojoFields = Array("name", "age"))

    // create a set from some given elements
    val values = env.fromElements("Foo", "bar", "foobar", "fubar")

    // generate a number sequence
    val numbers = env.generateSequence(1, 10000000)

    // read a file from the specified path of type SequenceFileInputFormat
//    val tuples = env.readSequenceFile(classOf[IntWritable], classOf[Text],
//      "hdfs://nnHost:nnPort/path/to/file")
  }
}
