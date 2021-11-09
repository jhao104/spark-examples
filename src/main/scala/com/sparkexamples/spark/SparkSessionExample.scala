package com.sparkexamples.spark

import org.apache.spark.sql.SparkSession

object SparkSessionExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    println("Spark Version: " + spark.version)
  }

}
