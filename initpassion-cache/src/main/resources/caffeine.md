# guava cache VS caffeine cache

### 缓存进化史

```
https://juejin.im/post/5b7593496fb9a009b62904fa
```

### HashMap

- 和缓存相比最大的缺点是不方便进行值回收

## guava cache

### 介绍

- refreshAfterWrite：当缓存项上一次更新操作之后的多久会被刷新

#### 缓存回收

- 基于容量回收(maximumSize(long)：当缓存中的元素数量超过指定值时)
- expireAfterAccess:  当缓存项在指定的时间段内没有被读或写就会被回收(指定时间段没有被访问)
- expireAfterWrite：当缓存项在指定的时间段内没有更新就会被回收(指定时间没有更新数据)
  - 只会有一个加载操作, 防止大量缓存同时失效导致缓存击穿而产生雪崩
- 基于引用回收
  - CacheBuilder.weakKeys()：使用弱引用存储键。当键没有其它（强或软）引用时，缓存项可以被垃圾回收。
  - CacheBuilder.weakValues()：使用弱引用存储值。当值没有其它（强或软）引用时，缓存项可以被垃圾回收。
  - CacheBuilder.softValues()：使用软引用存储值。软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收

#### 移除监听器

- CacheBuilder.removalListener(RemovalListener)

#### 统计

- CacheBuilder.recordStats()：用来开启Guava Cache的统计功能。统计打开后，Cache.stats()方法会返回CacheS tats 对象以提供如下统计信息：
- hitRate()：缓存命中率；
- averageLoadPenalty()：加载新值的平均时间，单位为纳秒；
- evictionCount()：缓存项被回收的总数，不包括显式清除。

#### 清理

- 个别清除：Cache.invalidate(key)
- 批量清除：Cache.invalidateAll(keys)
- 清除所有缓存项：Cache.invalidateAll()

#### 缺点

- expireAfterWrite: 限制只有1个加载操作时进行加锁，其他请求必须阻塞等待这个加载操作完成, 在加载完成之后，其他请求的线程会逐一获得锁，去判断是否已被加载完成，每个线程必须轮流地走一个“获得锁，获得值，释放锁“的过程，这样性能会有一些损耗, 如果加载太频繁, 系统资源消耗极大

## caffeine

#### 特性

- 自动将条目加载到缓存中，可选异步加载
- 当基于频率和最近度超过最大值时，基于尺寸的驱逐
- 基于时间的条目过期，从上次访问或上次写入开始计算
- 当出现第一个过时的条目请求时，异步刷新
- 自动封装在弱引用中的键
- 自动封装在弱引用或软引用中的值
- 退出(或以其他方式删除)条目的通知
- 传播到外部资源的写
- 缓存访问统计数据的积累

#### 使用案例

- spring5 和springboot2.0默认内部缓存

#### 初始化

```
Cache<String, DataObject> cache = Caffeine.newBuilder()
  .expireAfterWrite(1, TimeUnit.MINUTES)
  .maximumSize(100)
  .build();
```

#### 同步加载

```
LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
  .maximumSize(100)
  .expireAfterWrite(1, TimeUnit.MINUTES)
  .build(k -> DataObject.get("Data for " + k));
```

- 取值

  ```
  DataObject dataObject = cache.get(key);
  ```

- 获取全部值

  ```
  Map<String, DataObject> dataObjectMap = cache.getAll(Arrays.asList("A", "B", "C"));
  ```

#### 异步加载

```
AsyncLoadingCache<String, DataObject> cache = Caffeine.newBuilder()
  .maximumSize(100)
  .expireAfterWrite(1, TimeUnit.MINUTES)
  .buildAsync(k -> DataObject.get("Data for " + k));
```

#### 值回收

##### 基于大小，基于时间和基于引用

- 基于大小回收

  ```
  LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
    .maximumSize(1)
    .build(k -> DataObject.get("Data for " + k));
  ```

- 基于时间回收

  - **访问后过期**策略: expireAfterAccess:

    ```
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
      .expireAfterAccess(5, TimeUnit.MINUTES)
      .build(k -> DataObject.get("Data for " + k));
    ```

  - **写入后到期**策略: expireAfterWrite

    ```
    cache = Caffeine.newBuilder()
      .expireAfterWrite(10, TimeUnit.SECONDS)
      .weakKeys()
      .weakValues()
      .build(k -> DataObject.get("Data for " + k));
    
    ```

  - 自定义策略: 实现 **Expiry** 接口

    ```
    cache = Caffeine.newBuilder().expireAfter(new Expiry<String, DataObject>() {
        @Override
        public long expireAfterCreate(
          String key, DataObject value, long currentTime) {
            return value.getData().length() * 1000;
        }
        @Override
        public long expireAfterUpdate(
          String key, DataObject value, long currentTime, long currentDuration) {
            return currentDuration;
        }
        @Override
        public long expireAfterRead(
          String key, DataObject value, long currentTime, long currentDuration) {
            return currentDuration;
        }
    }).build(k -> DataObject.get("Data for " + k));
    
    ```

- 基于引用回收

  - Caffeine.weakKeys(), Caffeine.weakValues(),  Caffeine.softValues()

    ```
    LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
      .expireAfterWrite(10, TimeUnit.SECONDS)
      .weakKeys()
      .weakValues()
      .build(k -> DataObject.get("Data for " + k));
     
    cache = Caffeine.newBuilder()
      .expireAfterWrite(10, TimeUnit.SECONDS)
      .softValues()
      .build(k -> DataObject.get("Data for " + k));
    
    ```

#### 刷新

- 指定时间段后自动刷新

  ```
  Caffeine.newBuilder()
    .refreshAfterWrite(1, TimeUnit.MINUTES)
    .build(k -> DataObject.get("Data for " + k));
  
  ```

#### 统计(利用统计来调整最大缓存值, 过期时间和过期策略到最优配置)

```
LoadingCache<String, DataObject> cache = Caffeine.newBuilder()
  .maximumSize(100)
  .recordStats()
  .build(k -> DataObject.get("Data for " + k));
cache.get("A");
cache.get("A");

```





