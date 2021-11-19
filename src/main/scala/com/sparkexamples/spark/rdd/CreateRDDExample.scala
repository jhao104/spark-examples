package com.sparkexamples.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object CreateRDDExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    // Parallelize method
    val rdd1: RDD[Int] = sc.parallelize(Array(1,2,3,4,5))
    val rdd2: RDD[Int] = sc.parallelize(List(1,2,3,4,5), numSlices = 3)

    // Loading text file
    val rdd3 = sc.textFile("/path/textFile.txt")  // 文件路径

    // By transforming an RDD
    val logRDD = sc.textFile("/path/log.txt")
    val errorRDD = logRDD.filter(_.contains("ERROR"))

    // By transforming an DataFrame
    val rdd4 = spark.range(10).toDF().rdd
  }

}
