# docker安装zookeeper

## 拉取镜像

```
sudo docker pull daocloud.io/daocloud/zookeeper:3.4.10
```

## 运行容器

- 暴露三个端口
- 随着docker启动而启动
- pwc-mysql为之前创建的mysql容器

```
sudo docker run -it --volumes-from pwc-mysql --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 --restart always -d daocloud.io/daocloud/zookeeper:3.4.10
```

## 进入容器

```
sudo docker exec -it zookeeper /bin/bash
```

## 查看文件

```
ls
```

## 切换到conf目录

```
cd conf
```

## 编辑配置文件

```
vi zoo.cfg
```

```
dataDir=/data
dataLogDir=/datalog
tickTime=2000
initLimit=5
syncLimit=2
maxClientCnxns=60
```



