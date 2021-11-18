# Spark Parallelize 方法

Parallelize是一种在现有集合（例如数组）上创建RDD的方法。集合中的元素将被复制成为一个分布式数据集，然后我们可以在该数据集上进行并行操作。

Parallelize是Spark中创建RDD的三种方法之一，另外两种方法是:
* 从外部数据源创建，如本地文件系统、HDFS等等。
* 从现有RDD转换而来。

**用法**:
```scala
sc.parallelize(seq: Seq[T], numSlices: Int)
```

* `sc`： SparkContext 对象
* `seq`：集合对象
* `numSlices`：可选参数，创建数据集的分片数。默认情况下，Spark会根据集群的状况来自动设定slices的数目


## 示例

完整代码在[这里](https://github.com/jhao104/spark-examples/blob/master/src/main/scala/com/sparkexamples/spark/rdd/CreateRDDWithParallelize.scala)查看。

```scala
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
```

运行上面代码输出:

```shell
elements of rdd1:1,2,3,4,5
number of partitions:3
elements of rdd2:6,7,8,9,10
elements of rdd3:1,2,3,4,5,6,7,8,9,10
```

