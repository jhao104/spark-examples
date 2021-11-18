package com.sparkexamples.spark

import org.apache.spark.{SparkConf, SparkContext}

object SparkContextExample {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("SparkExample")
      .setMaster("local[1]")
    val sc = new SparkContext(sparkConf)
//    val sc = SparkContext.getOrCreate(sparkConf)

    println("Spark application unique identifier: " + sc.applicationId)
    println("spark.app.name: " + sc.appName)
    println("spark.master: " + sc.master)
  }

}
