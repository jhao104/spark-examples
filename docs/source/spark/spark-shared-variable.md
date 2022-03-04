# Spark — 共享变量

## 概述

在默认情况下，Spark在集群中多个节点运行一个函数时，它会把函数中涉及到的每个变量先创建一份副本，在副本上进行运算。但是有时候需要在多个任务之间共享变量，
或者在任务(Task)和Driver程序之间共享变量。为了满足这种需求，Spark提供了两种类型的变量：广播变量(broadcast variables)和累加器(accumulators)。

广播变量用来把变量在所有节点的内存之间进行共享。累加器则支持在不同节点之间进行累加计算(比如计数或者求和)。


## 累加器

累加器是仅仅被相关操作累加的变量，通常用来实现计数器(counter)和求和(sum)。如前面提到在各任务中同一函数中的变量是不共享的，在不使用累加器的情况下，
程序可能不会返回我们期望的值，例如：
```scala
var counter = 0
val rdd = sc.parallelize(Seq(1,2,3,4,5))
rdd.foreach(x => counter += x)
println(counter)
// 输出 0
```

上面代码并没有输出我们期望的 `15`，变量 `counter` 定义在Driver程序中，在执行时被累加的其实 `counter` 的副本，最后累加结果也不会更新到Driver程序的 `counter` 上，所以最后输出的值仍还是 `0` 。

下面我们使用累加器来稍微修改上面的程序:
```scala
val counter = sc.longAccumulator("counter")
val rdd = sc.parallelize(Seq(1,2,3,4,5))
rdd.foreach(x => counter.add(x))
println(counter.value)
// 输出 15
```

这样使用累加器 `longAccumulator` 程序便返回了我们期望的值。

SparkContext中提供了三种类型的累加器: `longAccumulator` 、 `doubleAccumulator` 、 `collectionAccumulator`，在创建时可以传入一个 `name` 参数，这样方便在Spark UI界面中查看。

累加器也是惰性计算的，只有在RDD进行Action操作时才会被更新，在遇到如Map等惰性操作时不会被立即计算，例如:
```scala
val counter = sc.longAccumulator("counter")
val rdd = sc.parallelize(Seq(1,2,3,4,5))
rdd.map{x => counter.add(x); x}
println(counter.value)
// 输出 0
```

## 广播变量



