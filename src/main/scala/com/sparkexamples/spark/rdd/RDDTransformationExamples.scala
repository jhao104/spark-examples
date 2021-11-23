package com.sparkexamples.spark.rdd

import org.apache.spark.sql.SparkSession

object RDDTransformationExamples {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    val rdd1 = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10))

    println("**** map example: ****")
    println(rdd1.map(x => 10 * x).collect.mkString(","))

    println("**** filter example: ****")
    println(rdd1.filter(x => x % 2 == 0).collect.mkString(","))
    println(rdd1.filter(x => x % 2 != 0).collect.mkString(","))

    println("**** flatMap example: ****")
    val fileRDD = sc.parallelize(List("this is the fist line", "this is the second line"))
    println(fileRDD.flatMap(x => x.split(" ")).collect.toSeq)

    println("**** mapPartitions example: ****")
    println(rdd1.mapPartitions(_.map(x => 10 * x)).collect.toSeq)

    println("**** mapPartitionsWithIndex example: ****")
    val rdd2 = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10), numSlices = 4)
    println(rdd2.mapPartitionsWithIndex((index,items)=>(items.map(x=>(index,x)))).collect.mkString(" - "))

    println("**** sample example: ****")
    println(rdd1.sample(withReplacement = false, 0.3).collect.mkString(" "))

    println("**** union example: ****")
    println(rdd1.union(rdd2).collect.mkString(" "))

    println("**** intersection example: ****")
    println(rdd1.intersection(rdd2).collect.mkString(" "))

    println("**** distinct example: ****")
    val rdd3 = sc.parallelize(Seq(1,1,2,2,3,4))
    println(rdd3.distinct.collect.mkString(" "))

    val rdd4 = sc.parallelize(Seq(("a", 1), ("b", 2), ("b", 3), ("c", 4), ("c", 5), ("d", 6)))
    println("**** groupByKey example: ****")
    rdd4.groupByKey.collect.foreach(println)

    println("**** reduceByKey example: ****")
    rdd4.reduceByKey(_+_).collect.foreach(println)

    val rdd5 = sc.parallelize(Seq(("a", 1), ("c", 2), ("b", 3), ("f", 4), ("e", 5), ("d", 6)))
    println("**** sortByKey example: ****")
    rdd5.sortByKey().collect.foreach(println)

    val rdd6 = sc.parallelize(Seq(("a", 1), ("b", 2), ("c", 3), ("d", 4)))
    val rdd7 = sc.parallelize(Seq(("b", 5), ("c", 6), ("d", 7), ("e", 8)))
    println("**** join example: ****")
    rdd6.join(rdd7).collect.foreach(println)

    println("**** cogroup example: ****")
    rdd6.cogroup(rdd7).collect.foreach(println)

    val rdd8 = sc.parallelize(1 to 3)
    val rdd9 = sc.parallelize(4 to 6)
    println("**** cartesian example: ****")
    rdd8.cartesian(rdd9).collect.foreach(println)

    println("**** coalesce example: ****")
    val rdd10 = sc.parallelize(1 to 10, 4)
    println("rdd10 NumPartitions: " + rdd10.getNumPartitions)
    val rdd11 = rdd10.coalesce(2)
    println("rdd11 NumPartitions: " + rdd11.getNumPartitions)

    println("**** repartition example: ****")
    val rdd12 = sc.parallelize(1 to 10, 2)
    println("rdd12 NumPartitions: " + rdd12.getNumPartitions)
    val rdd13 = rdd12.repartition(4)
    println("rdd13 NumPartitions: " + rdd13.getNumPartitions)

  }

}
