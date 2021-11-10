# Spark — SparkContext

SparkContext在Spark 1.x版本引入，在Spark 2.0引入SparkSession之前，它是Spark应用程序的切入点。

## SparkContext介绍

在Spark 1.x版本之后，SparkContext就成为Spark应用程序的切入点。
它定义在 `org.apache.Spark` 包中，用来在集群上创建RDD、累加器、广播变量。

每个JVM里只能存在一个处于active状态的SparkContext，在创建新的SparkContext之前必须调用 `stop()` 来关闭之前的SparkContext。

每一个Spark应用都是一个SparkContext实例，可以理解为一个SparkContext就是一个Spark应用的生命周期，
一旦SparkContext创建之后，就可以用这个SparkContext来创建RDD、累加器、广播变量。

## spark-shell中使用

Spark Shell默认提供了一个名为 `sc` 的SparkContext类的实例对象，我们可以在spark-shell中直接使用此对象。

```shell
scala> val rdd = sc.textFile("D:\\Code\\Scala\\word.txt")
```

## SparkContext创建(1.X)

在Scala中编写Spark应用程序需要先创建 `SparkConf` 实例，然后将SparkConf对象作为参数传递给 `SparkContext` 构造函数来创建SparkContext。

```scala
val sparkConf = new SparkConf()
  .setAppName("SparkExample")
  .setMaster("local[1]")
val sc = new SparkContext(sparkConf)
```

也可以使用 `getOrCreate()` 方法创建SparkContext。此函数用于获取或实例化SparkContext，并将其注册为单例对象。

```scala
val sc = SparkContext.getOrCreate(sparkConf)
```

## SparkContext创建(2.X之后)

Spark 2.0之后，我们主要使用[SparkSession](spark-session)，SparkSession中包含了SparkContext中大多数的API，Spark session在内部创建Spark Context并作为变量SparkContext来使用。

```scala
val sparkContext = spark.sparkContext
```

## SparkContext常用方法

* `*Accumulator()`：创建累加器变量。Spark内置了三种类型的Accumulator，分别是 `longAccumulator` 用来累加整数型， `doubleAccumulator` 用来累加浮点型， `collectionAccumulator` 用来累加集合元素
* `applicationId`：Spark应用的唯一标识
* `appName`：创建SparkContext时设置的AppName
* `broadcast()`：向集群广播一个只读变量，广播变量只会被发到各个节点一次
* `emptyRDD()`：创建一个空RDD
* `getPersistentRDDs`：返回所有持久化的RDD(`cache()`)
* `getOrCreate()`：创建或返回SparkContext
* `hadoopFile()`：根据Hadoop文件创建RDD
* `master`：创建SparkContext时设置的master
* `newAPIHadoopFile()`：使用新的API InputFormat为Hadoop文件创建RDD
* `sequenceFile()`：创建具有指定键和值类型的Hadoop SequenceFile的RDD
* `setLogLevel()`：设置日志级别
* `textFile()`：从HDFS、本地或任何Hadoop文件系统读取文本文件，返回RDD
* `union()`：Union两个RDD
* `wholeTextFiles()`：从HDFS、本地或任何Hadoop的文件系统读取文件夹中的文本文件，并返回Tuple2的RDD。元组的第一个元素为文件名，第二个元素为文件内容。