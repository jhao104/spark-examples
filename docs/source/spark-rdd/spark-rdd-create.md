# 创建RDD

RDD(Resilient Distributed Dataset)弹性分布式数据集是Spark中最重要的概念之一。它是一个只读的数据集合，之所以称它为弹性的，是因为它们能够在节点发生故障时根据血缘关系重新计算。

通常有下面四种创建RDD的方式:

* [Parallelize数据集合创建](create-rdd-from-parallelize)
* [加载外部数据源创建](create-rdd-from-text-file)
* [由RDD转换创建](create-rdd-from-rdd)
* [由DataFrame和DataSet转换创建](create-rdd-from-data-)

(create-rdd-from-parallelize)=
## Parallelize数据集合创建

可以将已存在的数据集合传递给SparkContext的 `parallelize` 方法创建RDD，详见[Spark Parallelize 方法](spark-rdd-parallelize.md)。

* 示例:
```scala
val rdd = sc.parallelize(Array(1,2,3,4,5))
```

* 指定分片数:
```scala
val rdd = sc.parallelize(List(1,2,3,4,5),3)  // 指定分片数 3
```

(create-rdd-from-text-file)=
## 加载外部数据源创建

Spark可以从所有Hadoop支持的存储源创建RDD，包括本地文件系统、HDFS、Cassandra、HBase、Amazon S3等等。它支持文本格式、SequenceFile和其他所有Hadoop InputFormat。

使用SparkContext的 `textFile` 方法创建文本文件RDD。此方法接收文件URI（本地文件路径或 `hdfs://` ， `s3a://` 等URI）作为参数，按行读取创建RDD。下面是一个调用示例：

```scala
val rdd = sc.textFile("/path/textFile.txt")  // 文件路径
```

(create-rdd-from-rdd)=
## 由RDD转换创建

可以使用转换算子如 `map`、`flatmap`、`filter`等从现有RDD创建新RDD。

假设这个场景, 从日志文件中找出含有 `ERROR` 字符串的行。我们先使用 `textFile` 加载日志文件创建RDD。然后再使用 `filter` 方法过滤出目标行，返回一个新的RDD，代码如下:

```scala
val logRDD = sc.textFile("/path/log.txt")
val errorRDD = logRDD.filter(_.contains("ERROR"))
```

(create-rdd-from-data-)=
## 由DataFrame和DataSet转换创建

DataSet和DataFrame的 `rdd` 属性将返回当前数据集的RDD形式。

```scala
val rdd = spark.range(10).toDF().rdd
```
