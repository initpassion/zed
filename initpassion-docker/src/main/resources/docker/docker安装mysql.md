#  docker安装mysql

## 拉取MySQL镜像

```
sudo docker pull mysql
```

## 查看镜像

```
sudo docker images
```

## 创建mysql镜像并启动

- –name：给新创建的容器命名
- -e：配置信息
- -p：端口映射
- -d：成功启动容器后输出容器的完整ID
- 最后一个"mysql"为镜像的名称

```
sudo docker run --name pwc-mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 -d mysql
```

## 查看状态是否成功启动

```
sudo docker ps -a
```

## 开放端口

```
sudo firewall-cmd --add-port=3306/tcp
```

## 关闭防火墙

```
sudo systemctl stop firewalld
```

## 进入到容器内部

```
docker exec -it pwc-mysql /bin/bash
```

## 执行命令进入数据库终端

```
mysql -hlocalhost -P3306 -uroot -p123456
```

