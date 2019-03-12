package com.sunll.flink.sql

import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.table.api.scala._
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{TableEnvironment, Types}
import org.apache.flink.table.sinks.CsvTableSink

/**
  * desc
  *
  * @author sunliangliang 2019-03-01 https://github.com/sunliangliang9201/tv_realtime_display
  * @version 1.0
  */
object TableSqlApp01 {

  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val tableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    val input: DataStream[(String, Int)] = env.fromElements(("a", 100), ("b", 200), ("c", 300), ("a", 300))
    //有三种方式可以使datastream或者dataset变成table，table如何变成dataset和datastream是调用.to...Stream和toDataset方法
    tableEnv.registerDataStream("t1", input, 'name, 'age)
    val table1 = tableEnv.fromDataStream(input, 'name1, 'age1)
    val tables = input.toTable(tableEnv, 'name2, 'age2)
    //tableEnv.sqlQuery("select age from t1").toRetractStream[Int].print()
    //table1.select("age1").toRetractStream[Int].print()
    //可以看出register table 和from table用的语法是不同的，一个是sql一个是table api
    //是可以互相转化的
//    val table2 = tableEnv.scan("t1")
//    table2.select("name").toRetractStream[String].print()
    tableEnv.registerTable("t2", table1)
    tableEnv.sqlQuery("select age1 from t2").toRetractStream[Int].print()
    tableEnv.scan("t1").printSchema()
    println(tableEnv.explain(table1))

    //结果写入sink表，必须调用insert
    val csvSink = new CsvTableSink("e:/test.csv")
    val fieldNames: Array[String] = Array("name", "age")
    val fieldTypes: Array[TypeInformation[_]] = Array(Types.STRING, Types.INT)
    tableEnv.registerTableSink("csvsink",fieldNames,fieldTypes,csvSink)
    table1.insertInto("csvsink")
    env.execute()

  }
}
