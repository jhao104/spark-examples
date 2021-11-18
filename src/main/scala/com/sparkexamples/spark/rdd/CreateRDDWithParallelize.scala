package com.sparkexamples.spark.rdd

import org.apache.spark.sql.SparkSession

object CreateRDDWithParallelize {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    val rdd1 = sc.parallelize(Array(1,2,3,4,5), 3)
    println("elements of rdd1:" + rdd1.collect().mkString(","))
    println("number of partitions:" + rdd1.getNumPartitions)

    val rdd2 = sc.parallelize(List(6,7,8,9,10))
    println("elements of rdd2:" + rdd2.collect().mkString(","))

    val rdd3 = rdd1.union(rdd2)
    println("elements of rdd3:" + rdd3.collect().mkString(","))

  }

}
