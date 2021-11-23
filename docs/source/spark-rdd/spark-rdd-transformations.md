# Spark RDD 转换算子(Transformations)

我们将RDD数据的转换操作称为**Transformations** ，RDD的所有转换操作都不会被计算，它仅记录作用于RDD上的操作，只有在遇到动作算子（Action）时才会进行计算。

下面是一些常见的转换算子：

```{list-table}
:widths: 10 40
:header-rows: 1
* - 转换
  - 说明
* - `map(func)`
  - 返回一个新的RDD，该RDD由每一个元素由 *func* 函数转换后组成。
* - `filter(func)`
  - 返回一个新的RDD，该RDD由经过 *func* 函数计算后返回值为true的元素组成。
* - `flatMap(func)`
  - 类似于 `map`，但是这里的 *func* 函数是将每个元素拆分成为0或多个元素输出，它应该返回一个 `Seq`，而不是一个元素。
* - `mapPartitions(func)`
  - 类似于 `map`，但它是在每个分区上运行 *func* 函数，因此当在类型为 *T* 的RDD上运行时，*func* 函数类型必须是 *Iterator[T] => Iterator[U]* 。
* - `mapPartitionsWithIndex(func)`
  - 类似于 `mapPartitions`，但它的 *func* 函数多一个当前分片索引号的参数，同 `mapPartitions` ，*func* 函数类型也必须是 *Iterator[T] => Iterator[U]* 。
* - `sample(withReplacement, fraction, seed)`
  - 数据抽样算子。
    <br> **withReplacement**：放回抽样或不放回抽样，*True* 表示放回抽样。
    <br> **fraction**：抽样比例，0-1之间的 *double* 类型参数，eg:0.3表示抽样30%。
    <br> **seed**：设置后会根据这个 *seed* 随机抽取。
* - `union(otherRDD)`
  - 将两个RDD中的元素进行合并取并集，类型要求一致，返回一个新的RDD。
* - `intersection(otherRDD)`
  - 将两个RDD中的元素进行合并取交集，类型要求一样，返回一个新的RDD。
* - `distinct([numPartitions])`
  - 将当前RDD进行去重后，返回一个新的RDD。
* - `groupByKey([numPartitions])`
  - 作用于Key-Value形式的RDD，将相同Key的值进行聚集，返回一个 *(K, Iterable[V])* 类型的RDD。
* - `reduceByKey(func, [numPartitions])`
  - 作用于Key-Value形式的RDD，将相同Key的值使用 *func* 进行reduce聚合，返回新的RDD。
* - `sortByKey([ascending], [numPartitions])`
  - 作用于Key-Value形式的RDD，根据Key进行排序。
* - `join(otherRDD, [numPartitions])`
  - 作用于Key-Value形式的RDD，将相同Key的数据join在一起。
* - `cogroup(otherRDD, [numPartitions])`
  - 作用于Key-Value形式的RDD，将相同key的数据分别聚合成一个集合。
* - `cartesian(otherRDD)`
  - 返回两个RDD的笛卡尔积RDD。
* - `pipe(command, [envVars])`
  - 执行一个外部脚本，返回输出的RDD。
* - `coalesce(numPartitions)`
  - 缩减分区数，用于大数据集过滤后提高小数据集的执行效率。
* - `repartition(numPartitions)`
  - 根据传入的分区数重新分区。
* - `repartitionAndSortWithinPartitions(partitioner)`
  - 重新分区+排序，这比先分区再排序效率高。
```

## 示例

### map

下面是一个简单的 `map` 示例：

```scala
val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10))
println(rdd.map(x => 10 * x).collect.mkString(","))
```

上面代码将输出：

```shell
10,20,30,40,50,60,70,80,90,100
```

### filter

比如分别过滤出1到10中的奇数和偶数，代码如下：

```scala
val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10))
println(rdd.filter(x => x % 2 == 0).collect.mkString(","))
println(rdd.filter(x => x % 2 != 0).collect.mkString(","))
```

上面代码将输出：

```shell
2,4,6,8,10
1,3,5,7,9
```

### flatMap

`flatMap` 的功能是将RDD中的每个元素进行拆分。 比如，假设我们读取了文本中的每一行，现在将其按空格拆分成单个词，代码如下：

```scala
val fileRDD = sc.parallelize(List("this is the fist line", "this is the second line"))
println(fileRDD.flatMap(x => x.split(" ")).collect.toSeq)
```

上面代码将输出：

```shell
WrappedArray(this, is, the, fist, line, this, is, the, second, line)
```

### mapPartitions

相对于 `map` 将 *func* 逐条应用于每条数据，`mapPartitions` 是在每个分区上一次性将所有数据应于函数 *func* 。 这样某些情况下具有更高的效率，示例：

```scala
val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10))
println(rdd.mapPartitions(_.map(x => 10 * x)).collect.toSeq)
```

上面代码将输出：

```shell
WrappedArray(10, 20, 30, 40, 50, 60, 70, 80, 90, 100)
```

```{note} 
虽然 `mapPartitions` 的效率优于 `map`，但是在内存有限的情况下可能会内存溢出。
```

### mapPartitionsWithIndex

类似 `mapPartitions`， 区别是 `mapPartitionsWithIndex` 的 *func* 函数多接受一个回调参数：当前分片的索引号。代码示例：

```scala
val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10), numSlices = 4)
println(rdd.mapPartitionsWithIndex((index,items)=>(items.map(x=>(index,x)))).collect().mkString(" - "))
```

上面代码将输出：

```shell
(0,1) - (0,2) - (1,3) - (1,4) - (1,5) - (2,6) - (2,7) - (3,8) - (3,9) - (3,10)
```

### sample

根据传入按比例进行有放回或者不放回的抽样。代码示例：

```scala
val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,8,9,10))
println(rdd.sample(withReplacement = false, 0.3).collect.mkString(" "))
```

上面代码将输出：

```shell
1 6 10
```

### union

将两个RDD中的元素进行合并取并集，类型要求一致，返回一个新的RDD。代码示例：

```scala
val rdd1 = sc.parallelize(1 to 5)
val rdd2= sc.parallelize(6 to 10)
println(rdd1.union(rdd2).collect.mkString(" "))
```

上面代码将输出：

```shell
1 2 3 4 5 6 7 8 9 10
```

### intersection

将两个RDD中的元素进行合并取交集，类型要求一样，返回一个新的RDD。代码示例：

```scala
val rdd1 = sc.parallelize(1 to 5)
val rdd2= sc.parallelize(3 to 8)
println(rdd1.intersection(rdd2).collect.mkString(" "))
```

上面代码将输出：

```shell
4 3 5
```

### distinct

对RDD里的元素进行去重操作。代码示例：

```scala
val rdd = sc.parallelize(Seq(1,1,2,2,3,4))
println(rdd.distinct.collect.mkString(" "))
```

上面代码将输出：

```shell
4 1 3 2
```

### groupByKey

`groupByKey` 用于将 *RDD[K,V]* 中相同 *K* 的值合并到一个集合 *Iterable[V]* 中。代码示例：

```scala
val rdd = sc.parallelize(Seq(("a", 1), ("b", 2), ("b", 3), ("c", 4), ("c", 5), ("d", 6)))
rdd.groupByKey.collect.foreach(println)
```

上面代码将输出：

```shell
(d,CompactBuffer(6))
(a,CompactBuffer(1))
(b,CompactBuffer(2, 3))
(c,CompactBuffer(4, 5))
```

### reduceByKey

`reduceByKey` 在 *RDD[K,V]* 上调用，返回一个 *RDD[K,V]* ，使用指定的reduce函数，将相同key的值聚合到一起。代码示例：

```scala
val rdd = sc.parallelize(Seq(("a", 1), ("b", 2), ("b", 3), ("c", 4), ("c", 5), ("d", 6)))
rdd.reduceByKey(_+_).collect.foreach(println)
```

上面代码将输出：

```shell
(d,6)
(a,1)
(b,5)
(c,9)
```

### reduceByKey

`reduceByKey` 在 *RDD[K,V]* 上调用，返回一个 *RDD[K,V]* ，使用指定的reduce函数，将相同key的值聚合到一起。代码示例：

```scala
val rdd = sc.parallelize(Seq(("a", 1), ("b", 2), ("b", 3), ("c", 4), ("c", 5), ("d", 6)))
rdd.reduceByKey(_+_).collect.foreach(println)
```

上面代码将输出：

```shell
(d,6)
(a,1)
(b,5)
(c,9)
```

### join

`join` 、 `fullOuterJoin` 、 `leftOuterJoin` 、 `rightOuterJoin` 都是针对 *RDD[K,V]* 中 *K* 值相同的连接操作，
分别对应内连接、全连接、左连接、右连接。代码示例：

```scala
val rdd1 = sc.parallelize(Seq(("a", 1), ("b", 2), ("c", 3), ("d", 4)))
val rdd2 = sc.parallelize(Seq(("b", 5), ("c", 6), ("d", 7), ("e", 8)))
rdd1.join(rdd2).collect.foreach(println)
```

上面代码将输出：

```shell
(d,(4,7))
(b,(2,5))
(c,(3,6))
```

### cogroup

代码示例：

```scala
val rdd1 = sc.parallelize(1 to 3)
val rdd2 = sc.parallelize(4 to 6)
rdd1.cartesian(rdd2).collect.foreach(println)
```

上面代码将输出：

```shell
(1,4)
(1,5)
(1,6)
(2,4)
(2,5)
(2,6)
(3,4)
(3,5)
(3,6)
```



### coalesce

`coalesce` 用来缩减分区，第二个可选参数 *shuffle* 是减少分区的过程中是否shuffle； *true* 为是， *false* 为否。默认为 *false* 。 代码示例：

```scala
val rdd1 = sc.parallelize(1 to 10, 4)
println("rdd1 NumPartitions: " + rdd1.getNumPartitions)
val rdd2 = rdd1.coalesce(2)
println("rdd2 NumPartitions: " + rdd2.getNumPartitions)
```

上面代码将输出：

```shell
rdd1 NumPartitions: 4
rdd2 NumPartitions: 2
```

### repartition

代码示例：

```scala
val rdd1 = sc.parallelize(1 to 10, 2)
println("rdd1 NumPartitions: " + rdd1.getNumPartitions)
val rdd2 = rdd1.repartition(4)
println("rdd2 NumPartitions: " + rdd2.getNumPartitions)
```

上面代码将输出：

```shell
rdd12 NumPartitions: 2
rdd13 NumPartitions: 4
```


## 完整代码

```scala
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
```

在线[查看](https://github.com/jhao104/spark-examples/blob/main/src/main/scala/com/sparkexamples/spark/rdd/RDDTransformationExamples.scala)。