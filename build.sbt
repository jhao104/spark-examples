name := "SparkExamples"

version := "0.1"

scalaVersion := "2.12.2"

enablePlugins(JavaAppPackaging)

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.3"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.3"

