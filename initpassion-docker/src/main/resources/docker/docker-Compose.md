# docker-Compose

## 三部曲

- Dockerfile 定义应用的运行环境
- docker-compose.yml 定义组成应用的各服务
- docker-compose up 启动整个应用

## 安装Compose

```
curl -L https://github.com/docker/compose/releases/download/1.8.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
```

```
chmod +x /usr/local/bin/docker-compose
```

## 查看版本信息

```
docker-compose --version
```

## 使用Compose

```
mkdir python
cd python
cat>app.py<<EOF 
```

```
from flask import Flask
from redis import Redis

app = Flask(__name__)
redis = Redis(host='redis', port=6379)

@app.route('/')
def hello():
    redis.incr('hits')
    return 'Hello World! I have been seen %s times.' % redis.get('hits')

if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=True)
```

```
vi requirements.txt
```

```
flask
redis
```

## 创建 Dockerfile

```
vi Dockerfile 
```

```
FROM python:2.7
ADD . /code
WORKDIR /code
RUN pip install -r requirements.txt
CMD python app.py
```

## 创建编排脚本

- 这个应用定义了两个服务：web, redis
- web容器通过当前路径下的Dockerfile生成
- web容器内的5000端口映射到主机的5000端口
- 将当前目录挂载到web容器内/code
- web容器依赖于redis容器
- redis容器从Docker Hub获取镜像

```
version: '2'
services:
  web:
    build: .
    ports:
     - "5000:5000"
    volumes:
     - .:/code
    depends_on:
     - redis
  redis:
    image: redis
```

## 启动应用

```
docker-compose up
```

## 访问应用

```
http://localhost:5000/
```

## daemon模式启动/停止

```
docker-compose up -d
```

```
docker-compose stop
```

## 查看信息

```
docker-compose ps
```

## 查看环境变量

```
docker-compose run web env
```

## 创建一个Wordpress应用

```
mkdir wordpress
```

```
cd wordpress
```

```
cat docker-compose.yml 
```

```
version: '2'
services:
  db:
    image: mysql:5.7
    volumes:
      - "./.data/db:/var/lib/mysql"
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: wordpress
      MYSQL_DATABASE: wordpress
      MYSQL_USER: wordpress
      MYSQL_PASSWORD: wordpress

  wordpress:
    depends_on:
      - db
    image: wordpress:latest
    links:
      - db
    ports:
      - "8000:80"
    restart: always
    environment:
      WORDPRESS_DB_HOST: db:3306
      WORDPRESS_DB_PASSWORD: wordpress
```

## 启动应用

```
docker-compose up -d
```

## 确认

```
docker-compose ps
```

## 访问应用

```
http://localhost:8000/
```

