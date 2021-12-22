package com.sparkexamples.spark

import org.apache.spark.sql.SparkSession

object WordCountExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext
    sc.textFile("src/main/resources/test.txt")
      .flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_+_)
      .saveAsTextFile("src/main/resources/result")
  }

}
