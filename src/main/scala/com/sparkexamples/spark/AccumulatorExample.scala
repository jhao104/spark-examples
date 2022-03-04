package com.sparkexamples.spark

import org.apache.spark.{SparkConf, SparkContext}

object AccumulatorExample {

  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf()
      .setAppName("AccumulatorExample")
      .setMaster("local[1]")
    val sc = new SparkContext(sparkConf)

    val rdd = sc.parallelize(Seq(1,2,3,4,5))

    // 不使用累加器
    var counter1 = 0
    rdd.foreach(x => counter1 += x)
    println("not use accumulator: " + counter1)

    // 使用累加器
    val counter2 = sc.longAccumulator("counter2")
    rdd.foreach(x => counter2.add(x))
    println("use accumulator: " + counter2.value)

    // 累加器惰性特性
    val counter3 = sc.longAccumulator("counter3")
    rdd.map{x => counter3.add(x); x}
    println("accumulator lazy evaluation: " + counter3.value)

  }

}