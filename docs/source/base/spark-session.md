# Spark — SparkSession

Apache Spark 2.0引入了 `SparkSession` ，它为用户提供了一个统一的切入点来使用Spark的各项功能，
并且允许用户通过它调用DataFrame和Dataset相关API来编写Spark程序。最重要的是，它减少了用户需要了解的一些概念，使得用户可以很容易地与Spark交互。

## SparkSession介绍

Spark 2.0引入了一个新类 `org.apache.Spark.sql.SparkSession` ，它内部封装了我们在2.0之前版本中
使用的所有上下文的组合类（ `SQLContext` 和 `HiveContext` 等等），使用起来更加方便。

正如前面所讲，SparkSession是Spark程序的切入点，所有Spark程序都应以创建SparkSession实例开始。
使用 `SparkSession.builder()` 创建SparkSession。

SparkSession包括了以下上下文接口:
* Spark Context
* SQL Context
* Streaming Context
* Hive Context

## spark-shell中使用

Spark Shell默认提供了一个名为 `spark` 的SparkSession类的实例对象，我们可以在spark-shell中直接使用此对象。

```shell
scala> val sqlContext = spark.sqlContext
```

## SparkSession创建

在Scala中创建SparkSession，需要使用生成器方法 `builder()` 然后调用 `getOrCreate()`方法。
如果SparkSession已经存在它将直接返回，否则将创建一个新的SparkSession:

```scala
val spark = SparkSession.builder()
  .master("local[1]")
  .appName("SparkExamples")
  .config("spark.sql.execution.arrow.enabled", "true")
  .getOrCreate();
```

* **master**：设置运行方式。 `local` 代表本机运行，`local[4]` 代表在本机分配4核运行。如果要在集群上运行，通常它可能是 `yarn` 或 `mesos`， 这取决于你的集群配置；
* **appName**：设置spark应用程序的名字，可以在web UI界面看到；
* **config**：额外配置项；
* **getOrCreate**：如果已经存在，则返回SparkSession对象；如果不存在，则创建新对象。

## SparkSession常用方法

* `version`：运行应用程序的Spark版本，或者是群集配置的Spark版本
* `builder()`：用来创建SparkSession实例，它返回 `SparkSession.Builder`
* `createDataFrame()`：将 `RDD` 、 `Seq` 、`java.util.List` 转换为 `DataFrame`
* `createDataset()`：将 `RDD` 、 `Seq` 、`java.util.List` 转换为 `Dataset`
* `emptyDataFrame()`：创建一个空的 `DataFrame`
* `emptyDataset()`：创建一个空的 `Dataset`
* `getActiveSession`：返回当前线程活跃的SparkSession
* `implicits`：常用Scala对象转换为DataFrame的隐式转换
* `read()`： 用于将csv、parquet、avro和其他文件格式的内容读取到DataFrame中， 它返回 `DataFrameReader` 实例
* `readStream()`：和read方法类似，不同的是它被用于读取流式数据
* `sparkContext`：返回 `SparkContext`
* `sql()`：执行SQL语句返回DataFrame数据
* `sqlContext`：返回 `SQLContext`
* `stop()`：停止当前SparkContext
* `table()`：返回DataFrame中的表或者视图
* `udf()`：创建Spark UDF(User Defined Functions)