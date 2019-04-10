# docker 安装redis

## 下载镜像

```sudo docker pull redis
sudo docker pull redis:4.0
```

## 创建数据目录

```
mkdir -p ~/redis ~/redis/data
```

## 启动

- **-d : 将容器的在后台运行**
- **-v $PWD/data:/data : 将主机中当前目录下的data挂载到容器的/data .redis数据卷,如未加上这个,容器重启后数据将丢失.**
- **–restart=always : 随docker启动而启动**

```
docker run -v $PWD/data:/data -d -p 6379:6379 redis:4.0 redis-server --appendonly yes --requirepass "ReDis@.1*1PWD"
```

## 连接查看容器

```
docker exec -it "containerId" redis-cli
```

