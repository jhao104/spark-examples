# Spark — 在IntelliJ IDEA上创建项目

## 安装Scala插件

推荐使用Scala编写Spark应用，在IDEA使用Scala需要先安装Scala插件。 位置: **File** > **Settings** > **Plugins**

```{figure} ../images/idea-scala-plugin.jpg
---
width: 70%
figclass: center-figure
alt: IDEA Scala Plugin
name: idea-scala-plugin
---
安装Scala插件
```

## 创建Scala项目

新建工程, 选择 **Scala** > **sbt** 

```{figure} ../images/create-scala-project.jpg
---
width: 70%
figclass: center-figure
alt: Create Scala Project
name: create-scala-project
---
```

设置项目名称和路径，选择Scala和SBT版本，点击Finish即完成创建:

```{figure} ../images/create-scala-project-1.jpg
---
width: 70%
figclass: center-figure
alt: Create Scala Project
name: create-scala-project-1
---
```

## 添加Sbt依赖

编辑 `build.sbt` 文件，添加Spark依赖包:

```Scala
name := "SparkExamples"

version := "0.1"

scalaVersion := "2.12.2"

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.3"
```

## 创建WordCount例子

在 `src/main/scala` 下创建 `WordCount.scala` 文件，代码如下:

```Scala
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object WordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf()
      .setAppName("WordCountExample")
      .setMaster("local[2]")
    val sc = new SparkContext(conf)
    val textFile = sc.textFile("D:\\Code\\Scala\\word.txt")
    val wordCount = textFile.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _)
    wordCount.foreach(println)
  }
}
```

右键 `WordCount.scala` 选择运行即可:

```{figure} ../images/run-word-count.jpg
---
width: 90%
figclass: center-figure
alt: Run Word Count
name: run-word-count
---
```