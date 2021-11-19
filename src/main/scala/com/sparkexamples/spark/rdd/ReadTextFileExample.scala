package com.sparkexamples.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object ReadTextFileExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    // 读取单个文件
    val rdd1:RDD[String] = sc.textFile("/path/test01.txt")
    rdd1.foreach(println)

    // 读取多个文件
    val rdd2:RDD[String] = sc.textFile("/path/test01.txt,/path/test02.txt")
    rdd2.foreach(println)

    // 读取路径下所有文件
    val rdd3:RDD[String] = sc.textFile("/path/resources/")
    rdd3.foreach(println)

    // 通配符示例-读取路径所有txt文件
    val rdd4:RDD[String] = sc.textFile("/path/*.txt")
    rdd4.foreach(println)

    // 通配符示例-读取多个目录下的文件
    val rdd5:RDD[String] = sc.textFile("/path/*/*.txt")
    rdd5.foreach(println)

    // 读取HDFS文件
    val rdd6:RDD[String] = sc.textFile("hdfs://master:9000/examples/data.txt")
    rdd6.collect.foreach(println)
  }
}
