# Docker安装使用

## 卸载旧版本

```
sudo yum remove docker  docker-common docker-selinux docker-engine
```

## 安装需要的软件包

```
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```

## 设置yum源

```
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
```

## 查看所有仓库中所有docker版本，并选择特定版本安装

```
yum list docker-ce --showduplicates | sort -r
```

## 安装docker

```
sudo yum install docker-ce-17.12.0.ce  #由于repo中默认只开启stable仓库，故这里安装的是最新稳定版17.12.0
```

## 启动并加入开机启动

```
sudo systemctl start docker
```

```
sudo systemctl enable docker
```

## 验证安装是否成功(包含client和service两部分)

```
docker version
```

## docker删除容器
```
docker rm dockerId

```

## docker删除镜像
```
docker rmi imgaeId
```
## rmi -f 发现镜像不存在时

- 切换到root用户
- ```service docker stop```
- ```rm -rf /var/lib/docker```
- ```重启 sudo service docker restart```
- 以上步骤会删除所有的镜像

