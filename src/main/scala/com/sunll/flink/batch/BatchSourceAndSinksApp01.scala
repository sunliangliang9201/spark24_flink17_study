package com.sunll.flink.batch

import com.sunll.spark.sql.DSApp.Person
import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.configuration.Configuration
import org.apache.flink.core.fs.FileSystem.WriteMode
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.flink.api.scala._
/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object BatchSourceAndSinksApp01 {

  def main(args: Array[String]): Unit = {
    val env  = ExecutionEnvironment.getExecutionEnvironment


    //sources
    // read text file from local files system
    val localLines = env.readTextFile("file:///path/to/my/textfile")

    // read text file from a HDFS running at nnHost:nnPort
    val parameters = new Configuration
    // set the recursive enumeration parameter
    parameters.setBoolean("recursive.file.enumeration", true)//是否递归读取文件
    val hdfsLines = env.readTextFile("hdfs://nnHost:nnPort/path/to/my/textDir").withParameters(parameters)

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



    //sinks
    // text data
    val textData: DataSet[String] = env.readTextFile("file:///path/to/my/textfile")
    // write DataSet to a file on the local file system
    textData.writeAsText("file:///my/result/on/localFS")

    // write DataSet to a file on a HDFS with a namenode running at nnHost:nnPort
    textData.writeAsText("hdfs://nnHost:nnPort/my/result/on/localFS")

    // write DataSet to a file and overwrite the file if it exists
    textData.writeAsText("file:///my/result/on/localFS", WriteMode.OVERWRITE)

    // tuples as lines with pipe as the separator "a|b|c"
    val values2: DataSet[(String, Int, Double)] = env.readCsvFile[(String, Int, Double)]("hdfs:///the/CSV/file")
    values2.writeAsCsv("file:///path/to/the/result/file", "\n", "|")

    // this writes tuples in the text formatting "(a, b, c)", rather than as CSV lines
    values2.writeAsText("file:///path/to/the/result/file")

    // this writes values as strings using a user-defined formatting
    values2.map{ tuple => tuple._1 + " - " + tuple._2 }
      .writeAsText("file:///path/to/the/result/file")
  }
}
