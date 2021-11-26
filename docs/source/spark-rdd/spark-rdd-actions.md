# Spark RDD 行动算子(Actions)

正如[转换算子](spark-rdd-transformations)中提到的，所有的转换算子都是惰性的，这意味着它们不会立即执行，只有在遇到行动算子时才会触发执行。

Spark中的行动算子分为两类：一类算子执行转换算子生成Scala集合或变量；另一类是将RDD保存到外部文件系统或者数据库中。

下面是一些常见的行动算子：

```{list-table}
:widths: 10 40
:header-rows: 1
* - Action
  - 说明
* - `reduce(func)`
  - 使用函数 *func* 聚合RDD中的所有元素。该函数应该是可交换和关联的。
* - `collect()`
  - 以数组形式返回所有的数据。
* - `count()`
  - 返回 *RDD* 中元素个数。
* - `first()`
  - 返回 *RDD* 中第一个元素。
* - `take(n)`
  - 返回 *RDD* 中前 *n* 个元素组成的数据。
* - `takeSample(withReplacement, num, [seed])`
  - 返回一个数组，该数组由从 *RDD* 中随机采样的 *num* 个元素组成返回。
* - `takeOrdered(n, [ordering])`
  - 按 *RDD* 的自然顺序或自定义比较器返回 *RDD* 的前 *n* 个元素。
* - `saveAsTextFile(path)`
  - 将 *RDD* 中的元素以textfile的形式保存到HDFS文件系统或者其他支持的文件系统，对于每个元素，Spark将会调用 `toString` 方法，将它装换为文件中的文本。
* - `saveAsSequenceFile(path)`
  - 将 *RDD* 中的元素以Hadoop sequencefile的格式保存到指定的目录下，可以是HDFS或者其他Hadoop支持的文件系统。
* - `countByKey()`
  - 应用于 *(K,V)* 类型的 *RDD* ，返回一个 *(K,Int)* 的map，表示每一个key对应的元素个数。
* - `foreach(func)`
  - 在 *RDD* 中的每一个元素上运行函数 *func* 。
```

## 示例

### reduce

`reduce` 常用来整合RDD中所有数据，例如求和操作。 示例：

```scala
val rdd = sc.parallelize(1 to 10)
println(rdd.reduce((x, y) => x + y))
// 输出: 55
```

上面的函数部分也可简写为：

```scala
rdd.reduce(_+_)
```

### collect

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println(rdd.collect().mkString(" "))
// 输出: 1 2 3 4 5 6 7 8 9 10
```

```{note} 
`collect` 应该在 `filter` 或其他操作后返回一个足够小的数据集时使用。 如果直接将整个数据集 `collect` 返回，这可能会使driver程序OOM。
```

### count

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println("elements number:" + rdd.count())
// 输出: elements number:10
```

### first

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println("the first element:" + rdd.first())
// 输出: the first element:1
```

### take

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println("the first two element:" + rdd.take(2).mkString(" "))
// 输出: the first two element:1 2
```

```{note} 
仅当预期生成的数组很小时才应使用此方法，因为所有数据都将加载到driver内存中。
```

### takeSample

同 `take` 方法，该方法仅在预期结果数组很小的情况下使用，因为所有数据都被加载到driver内存中。

语法:
```scala
takeSample(withReplacement: Boolean, num: Int, seed: Long)
```
* withReplacement：元素是否可以多次抽样；
* num：返回样本大小；
* seed：随机数生成器的种子；

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println("take two sample element:" + rdd1.takeSample(withReplacement = true,2).mkString(" "))
// 输出: the first two element:3 10
```

### takeOrdered

同 `take` 方法，该方法仅在预期结果数组很小的情况下使用，因为所有数据都被加载到driver内存中。

示例：

```scala
val rdd = sc.parallelize(1 to 10)
println("take two element with order:" + rdd.takeOrdered(2).mkString(" "))
// 输出: take two element with order:1 2
```

### saveAsTextFile

示例：

```scala
val rdd = sc.parallelize(1 to 10)
rdd1.saveAsTextFile("path/data")
```

### countByKey

示例：

```scala
val rdd = sc.parallelize(Seq("a", "b", "b", "c", "c", "c", "d"))
println(rdd.map(x => (x, 1)).countByKey())
// 输出: Map(d -> 1, a -> 1, b -> 2, c -> 3)
```

## 完整代码

```scala
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
```

在线[查看](https://github.com/jhao104/spark-examples/blob/main/src/main/scala/com/sparkexamples/spark/rdd/RDDActionExamples.scala)。