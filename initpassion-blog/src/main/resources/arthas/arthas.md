# <Center>Arthas实践</center>

#### dashboard(程序大盘)

#### thread(线程信息)

- thread 全部线程
- thread -n 3最忙碌的前三个线程
- thread 1 展示线程1的信息
- thread -b 找出阻塞其他线程的线程(罪魁祸首)
- thread -i 指定采样时间间隔

#### jad(反编译代码)

- 命令 

  ```
  jad com.initpassion.core.InitpassionCoreApplication
  ```

- 结果

  - ClassLoader(显示该类的加载器)

    ```
    +-sun.misc.Launcher$AppClassLoader@18b4aac2
    	+-sun.misc.Launcher$ExtClassLoader@4c98385c 
    ```

  - Location

    ```
    /E:/work/zed/initpassion-core/target/classes/
    ```

  - 源码

#### watch(支持合法的ognl表达式)

- 查看方法返回值(回到案发现场,实时查看方法返回值)

  ```
  watch com.initpassion.core.InitpassionCoreApplication primeFactors returnObj
  ```

- 查看方法的第一个参数

  ```
  watch com.initpassion.core.InitpassionCoreApplication print "params[0]"
  ```

- 查看第二个参数的size

  ```
  watch com.initpassion.core.InitpassionCoreApplication print "params[1].size()"
  ```

- 根据参数条件过滤

  ```
   watch com.initpassion.core.InitpassionCoreApplication print "params[0].{? #this >=100 }" -x 2
  ```

- 调用静态方法

  ```
   watch com.initpassion.core.InitpassionCoreApplication print "@java.lang.Thread@currentThread()"
  ```

- 访问静态变量(例:观察缓存map中有哪些值)

  ```
  getstatic com.initpassion.core.InitpassionCoreApplication cache
  ```

  ​

