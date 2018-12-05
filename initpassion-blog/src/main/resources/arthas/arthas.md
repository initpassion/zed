# <Center>Arthas实践</center>

#### dashboard(程序大盘)

#### 常见参数说明

`-n` 标识次数或者条数

`-x`标识深度

`-e` 抛出异常

`-i` 时间间隔

`-b `  加在thread后面表示线程被阻塞的罪魁祸首

`-b` 加在watch后面表示方法调用前 

`-e` 方法异常后

`-s` 方法返回后

`-f` 加在watch后面表示方法结束后

`-f` 加在SC后面表示方法成员变量信息

`-d` 详细内容

`-e` 正则表达式

`-c` 放在monitor后面表示统计周期

`-j`  jdkMethodSkip, skip jdk method trace



#### thread(线程信息)

- thread 全部线程
- thread -n 3最忙碌的前三个线程
- thread 1 展示线程1的信息
- thread -b 找出阻塞其他线程的线程(罪魁祸首)
- thread -i 指定采样时间间隔

#### jad(反编译代码)

- 命令 

  ```
  jad com.initpassion.demo.as.InitpassionAsDemoApplication
  ```

- 结果

  - ClassLoader(显示该类的加载器)

    ```
    +-sun.misc.Launcher$AppClassLoader@18b4aac2
    	+-sun.misc.Launcher$ExtClassLoader@4c98385c 
    ```

  - Location

    ```
    /E:/work/zed/initpassion-demo/target/classes/
    ```

  - 源码

- 反编译具体的方法名称

  ```
  jad com.initpassion.demo.as.InitpassionAsDemoApplication main
  ```

  ​

#### watch(支持合法的ognl表达式)

- watch 命令定义了4个观察事件点，即 `-b` 方法调用前，`-e` 方法异常后，`-s` 方法返回后，`-f` 方法结束后(类似AOP)


- 查看方法返回值(回到案发现场,实时查看方法返回值)

  ```
  watch com.initpassion.demo.as.InitpassionAsDemoApplication primeFactors returnObj
  ```

- 查看方法的第一个参数

  ```
  watch com.initpassion.demo.as.InitpassionAsDemoApplication print "params[0]"
  ```

- 查看第二个参数的size

  ```
  watch com.initpassion.demo.as.InitpassionAsDemoApplication print "params[1].size()"
  ```

- 根据参数条件过滤

  ```
   watch com.initpassion.demo.as.InitpassionAsDemoApplication print "params[0].{? #this >=100 }" -x 2
  ```

- 调用静态方法

  ```
   watch com.initpassion.demo.as.InitpassionAsDemoApplication print "@java.lang.Thread@currentThread()"
  ```

- 访问静态变量(例:观察缓存map中有哪些值)

  ```
  getstatic com.initpassion.demo.as.InitpassionAsDemoApplication cache
  ```

#### SC(查看JVM已加载的类信息)

- 模糊查询

  ```
  sc com.initpassion.*
  ```

- 查看类的详细信息

  ```
   sc -d com.initpassion.demo.as.InitpassionAsDemoApplication
  ```

- 查看类的成员变量

  ```
   sc -d -f com.initpassion.demo.as.InitpassionAsDemoApplication
  ```

#### sm(查看JVM已加载的类的方法信息)

- 查看String类有哪些方法

  ```
   sm java.lang.String
  ```

- 查看String.toString()

  ```
  sm -d java.lang.String toString
  ```

#### jvm(查看当前JVM信息)

- jvm
  - 结果中Thread信息
    - COUNT: JVM当前活跃的线程数
    - DAEMON-COUNT: JVM当前活跃的守护线程数
    - PEAK-COUNT: 从JVM启动开始曾经活着的最大线程数
    - STARTED-COUNT: 从JVM启动开始总共启动过的线程次数
    - DEADLOCK-COUNT: JVM当前死锁的线程数

#### classloader

- 按类加载实例进行统计

  ```
  classloader -l
  ```

- 查看继承关系

  ```
  classloader -t
  ```

- 查看实际的urls

  ```
  classloader -c 3d4eac69
  ```

#### monitor(方法执行监控)

- 查看方法执行

  ```
   monitor -c 5 com.initpassion.demo.as.InitpassionAsDemoApplication primeFactors
  ```

#### trace(方法内部调用路径,并输出每个节点耗时)

- 查看最耗时的方法

  ```
   trace com.initpassion.demo.as.InitpassionAsDemoApplication run
  ```

- 根据耗时进行过滤

  ```
  trace com.initpassion.demo.as.InitpassionAsDemoApplication run '#cost > 10'
  ```

#### tt(方法执行数据的时空隧道,案发现场)

- 记录下当前方法的每次调用环境现场

  ```
   tt -t com.initpassion.demo.as.InitpassionAsDemoApplication primeFactors
  ```

  ​





