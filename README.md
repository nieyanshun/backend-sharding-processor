# backend-sharding-processor

spring+jdbctemplate搭建，用于可分片场景作业批处理

执行：java -jar *.jar

工程起因

---
```
某天工作中要求我去处理一批数据，从大数据平台上拉下来的数据有700W，需要对这批数据进行基本信息填充（使用http api）

所以写了这个模型去处理数据，开始线程池设置为:
cpu核心数*3/4，我们机器是4核，因此我设为3，避免拉低服务器性能，后来发现，3线程while循环批处理，cpu10%都不到。

8线程，数个小时处理完这批数据。

另外，服务器jdk只有jcmd工具，这里记录下一些命令
打印线程栈信息
jcmd pid Thread.print

ps H -eo user,pid,ppid,%cpu --sort=cpu%
获取占用cpu最大的线程，将线程id转换为十六进制，在线程栈中nid即为相应线程id

```


---

