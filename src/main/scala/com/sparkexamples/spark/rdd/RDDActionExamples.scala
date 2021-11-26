package com.sparkexamples.spark.rdd

import org.apache.spark.sql.SparkSession

object RDDActionExamples {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    val rdd1 = sc.parallelize(1 to 10)

    println("**** reduce example: ****")
    println(rdd1.reduce((x, y) => x + y))

    println("**** collect example: ****")
    println(rdd1.collect().mkString(" "))

    println("**** count example: ****")
    println("elements number:" + rdd1.count())

    println("**** first example: ****")
    println("the first element:" + rdd1.first())

    println("**** take example: ****")
    println("the first two element:" + rdd1.take(2).mkString(" "))

    println("**** takeSample example: ****")
    println("take two sample element:" + rdd1.takeSample(withReplacement = true,2).mkString(" "))

    println("**** takeOrdered example: ****")
    println("take two element with order:" + rdd1.takeOrdered(2).mkString(" "))

    println("**** saveAsTextFile example: ****")
    rdd1.saveAsTextFile("path/data")

    println("**** saveAsTextFile example: ****")
    val rdd2 = sc.parallelize(Seq("a", "b", "b", "c", "c", "c", "d"))
    println(rdd2.map(x => (x, 1)).countByKey())

  }

}
