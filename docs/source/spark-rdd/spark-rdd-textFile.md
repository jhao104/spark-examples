# SparkContext.textFile读取文件

SparkContext提供了 `textFile()` 方法用于按行读取文本文件，返回RDD。

**用法**:
```scala
sc.textFile(path: String, minPartitions: Int)
```

* `sc`： SparkContext 对象
* `path`：本地文件路径或 `hdfs://` ， `s3a://` 等Hadoop支持的文件系统URI
* `minPartitions`：可选参数，指定数据的最小分区

默认情况下，Spark为文件的每个块创建一个分区（HDFS中一个块默认为128MB），可以通过 `minPartitions` 参数来设置更多分区。请注意，分区数不能少于块数。

## 读取单个文件

直接将文件路径作为 `path` 参数传入 `textFile()` 读取单个文件，返回每行内容的RDD。

```scala
val rdd = sc.textFile("/path/text.txt")
```

## 读取多个文件

如果要读取的多个指定文件，使用逗号分隔文件名传入 `textFile()` 即可。

```scala
val rdd = sc.textFile("/path/test01.txt,/path/test02.txt")
```

## 读取路径下所有文件

`path` 可传入文件路径来读取路径下所有文件。

```scala
val rdd = sc.textFile("/path/")
```

## 使用通配符读取多个文件

文件路径中可以使用通配符来读取多个文件。

```scala
val rdd1 = sc.textFile("/path/*.txt")  // 读取路径所有txt文件
val rdd2 = sc.textFile("/path/*/*.txt")  // 读取多个目录下的文件
```

## 从HDFS读取文件

从HDFS中读取文件和读取本地文件一样，只是要在URI中表明是HDFS。上面的所有读取方式也都适用于HDFS。

```scala
val rdd = sc.textFile("hdfs://master:9000/examples/data.txt")
```

## 完整代码

```scala
package com.sparkexamples.spark.rdd

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object ReadTextFileExample {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkExample")
      .getOrCreate()

    val sc = spark.sparkContext

    // 读取单个文件
    val rdd1:RDD[String] = sc.textFile("/path/test01.txt")
    rdd1.foreach(println)

    // 读取多个文件
    val rdd2:RDD[String] = sc.textFile("/path/test01.txt,/path/test02.txt")
    rdd2.foreach(println)

    // 读取路径下所有文件
    val rdd3:RDD[String] = sc.textFile("/path/resources/")
    rdd3.foreach(println)

    // 通配符示例-读取路径所有txt文件
    val rdd4:RDD[String] = sc.textFile("/path/*.txt")
    rdd4.foreach(println)

    // 通配符示例-读取多个目录下的文件
    val rdd5:RDD[String] = sc.textFile("/path/*/*.txt")
    rdd5.foreach(println)

    // 读取HDFS文件
    val rdd6:RDD[String] = sc.textFile("hdfs://master:9000/examples/data.txt")
    rdd6.collect.foreach(println)
  }
}
```

在线[查看](https://github.com/jhao104/spark-examples/blob/main/src/main/scala/com/sparkexamples/spark/rdd/ReadTextFileExample.scala)。

