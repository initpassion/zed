## 常见命令
-  sudo docker search mysql 搜索
-  sudo docker pull mysql:5.6 官网拉取镜像
-  sudo docker run -t -i thismysql /bin/bash
-  sudo docker images 查看镜像
-  sudo docker ps -a 查看运行中的镜像进程
-  sudo docker rm -f thismysql 删除mysql容器
-  sudo docker rmi imgid 删除镜像文件
-  sudo docker run --name thismysql -p 3308:3306 -e MYSQL_ROOT_PASSWORD=root-password -d mysql:5.6
- sudo docker exec -i -t thismysql /bin/bash 进入到容器内
- sudo docker logs thismysql
- -d 在后台运行 ;
- sudo docker run --name masterdb -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -v /db/mysqlCluster/master:/var/lib/mysql -d mysql:5.6 创建主数据库
- apt-get update && apt-get -yq install vim 安装vim
- 


 sudo docker run -itd -p 3308:3306 --name slave -v /home/nisp/mysqlData/slave/conf:/etc/mysql/conf.d -v /home/nisp/mysqlData/slave/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD='root' mysql:5.6

## docker mysql主从
Docker mysql 主从集群

主机A上建立主mysql

- 建立Dockerfile
      FROM mysql:5.6
      set timezone as china/shanghai
      RUN cp /usr/share/zoneinfo/PRC /etc/localtime
      copy mysql config file
      COPY master.cnf /etc/mysql/conf.d/
      ENTRYPOINT ["docker-entrypoint.sh"]
      EXPOSE 3306
      CMD ["mysqld"]
- 建立主master.cnf
       [mysqldump]
      	user=root
      	password='123456'
       [mysqld]
          max_allowed_packet=8M
          lower_case_table_names=1
          character_set_server=utf8
          max_connections=900
          max_connect_errors=600
          server-id=1
          log-bin=mysql-bin
          slow_query_log=1
          long_query_time=1
          log_error
- 构建出主mysql镜像

    docker build -t yidun/mysql:master .

- 启动主mysql容器

    docker run --name master -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -V /home/nisp/mysql/data:/var/lib/mysql -d yidun/mysql:master

- 进入容器

    docker exec -it master /bin/bash

- 进入到mysql终端

    mysql -uroot -p123456

- 授权从库所使用的账号

    GRANT REPLICATION SLAVE ON *.* TO 'slave-user'@'主机A的IP' IDENTIFIED BY 'slave-password';//指定ip
    或者
    GRANT REPLICATION SLAVE ON *.* to 'calve-user'@'%' identified by 'slave-password'; //所有ip

- 查看主库状态

    show master status;//记录File 的值和Position的值

主机B上建立从mysql

- 建立Dockerfile
      FROM mysql:5.6
      set timezone as china/shanghai
      RUN cp /usr/share/zoneinfo/PRC /etc/localtime
      copy mysql config file
      COPY slave.cnf /etc/mysql/conf.d/
      ENTRYPOINT ["docker-entrypoint.sh"]
      EXPOSE 3306
      CMD ["mysqld"]
- 建立slave.cnf
       [mysqldump]
      	user=root
      	password='123456'
       [mysqld]
      	max_allowed_packet=8M
      	lower_case_table_names=1
      	character_set_server=utf8
       	max_connections=900
      	max_connect_errors=600
      	log-bin=mysql-bin
      	slow_query_log=1
      	long_query_time=1
      	#replicate-wild-do-table=db1.% //指定需要同步的数据库
        	#replicate-wild-do-table=db2.%
        	log_error
        	# Regard this db as a slave
        	server-id=2
- 构建出从mysql镜像

    docker build -t yidun/mysql:slave .

- 启动主mysql容器

    docker run --name slave -p 3308:3306 -e MYSQL_ROOT_PASSWORD=123456  -V /home/nisp/mysql/data:/var/lib/mysql -d yidun/mysql:slave

- 进入容器

    docker exec -it slave /bin/bash

- 进入到mysql终端

    mysql -uroot -p123456

- 建立主库连接

    change master to
    master_host='主机A的IP',
    master_user='slave-user',//主机授权给从库的用户
    master_log_file='mysql-bin.000004',//主机File 的值
    master_log_pos=334,//主机Position的值
    master_port=3307,//主机指定的ip
    master_password='slave-password';//主机授权给从库的密码

- 启动从库

     start slave;

- 查看从库状态

    show slave status\G;// Slave_IO_Running: Yes ; Slave_SQL_Running: Yes 时表示成功连接

- 从库状态示例

                 Slave_IO_State: Waiting for master to send event
                      Master_Host: 10.122.167.182
                      Master_User: slave-user
                      Master_Port: 3307
                    Connect_Retry: 60
                  Master_Log_File: mysql-bin.000004
              Read_Master_Log_Pos: 1338
                   Relay_Log_File: 8d1e3b87d499-relay-bin.000002
                    Relay_Log_Pos: 1221
            Relay_Master_Log_File: mysql-bin.000004
                 Slave_IO_Running: Yes
                Slave_SQL_Running: Yes
                  Replicate_Do_DB: 
              Replicate_Ignore_DB: 
               Replicate_Do_Table: 
           Replicate_Ignore_Table: 
          Replicate_Wild_Do_Table: 
      Replicate_Wild_Ignore_Table: 
                       Last_Errno: 0
                       Last_Error: 
                     Skip_Counter: 0
              Exec_Master_Log_Pos: 1338
                  Relay_Log_Space: 1435
                  Until_Condition: None
                   Until_Log_File: 
                    Until_Log_Pos: 0
               Master_SSL_Allowed: No
               Master_SSL_CA_File: 
               Master_SSL_CA_Path: 
                  Master_SSL_Cert: 
                Master_SSL_Cipher: 
                   Master_SSL_Key: 
            Seconds_Behind_Master: 0
    Master_SSL_Verify_Server_Cert: No
                    Last_IO_Errno: 0
                    Last_IO_Error: 
                   Last_SQL_Errno: 0
                   Last_SQL_Error: 
      Replicate_Ignore_Server_Ids: 
                 Master_Server_Id: 1
                      Master_UUID: cd327a00-5e18-11e7-98f7-0242ac110006
                 Master_Info_File: /var/lib/mysql/master.info
                        SQL_Delay: 0
              SQL_Remaining_Delay: NULL
          Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
               Master_Retry_Count: 86400
                      Master_Bind: 
          Last_IO_Error_Timestamp: 
         Last_SQL_Error_Timestamp: 
                   Master_SSL_Crl: 
               Master_SSL_Crlpath: 
               Retrieved_Gtid_Set: 
                Executed_Gtid_Set: 
                    Auto_Position: 0
             Replicate_Rewrite_DB: 
                     Channel_Name: 
               Master_TLS_Version: 


#### 主从创建

Docker mysql主从配置

- 建立Dockerfile
      FROM mysql:5.6
      # set timezone as china/shanghai
      RUN cp /usr/share/zoneinfo/PRC /etc/localtime
      # copy mysql config file
      ENTRYPOINT ["docker-entrypoint.sh"]
      EXPOSE 3306
      CMD ["mysqld"]

- 构建镜像
      docker build -t yidun/mysql:5.6 ./
  
- 建立主库的my.cnf
      [client]
      port    = 3306
      socket   = /var/run/mysqld/mysqld.sock
      [mysqld_safe]
      pid-file  = /var/run/mysqld/mysqld.pid
      socket   = /var/run/mysqld/mysqld.sock
      nice    = 0
      [mysqld]
      user    = mysql
      pid-file  = /var/run/mysqld/mysqld.pid
      socket   = /var/run/mysqld/mysqld.sock
      port    = 3306
      basedir   = /usr
      datadir   = /var/lib/mysql
      tmpdir   = /tmp
      lc-messages-dir = /usr/share/mysql
      explicit_defaults_for_timestamp
      log-bin = mysql-bin
      server-id = 1
      # Instead of skip-networking the default is now to listen only on
      # localhost which is more compatible and is not less secure.
      #bind-address  = 127.0.0.1
      #log-error = /var/log/mysql/error.log
      # Recommended in standard MySQL setup
      sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
      symbolic-links=0
      !includedir /etc/mysql/conf.d/
- 运行主库容器
  - 参数说明 
    - -p 3307:3306 映射mysql端口
    - -e MYSQL_ROOT_PASSWORD=123456 指定管理员(root)密码
    - -e MYSQL_USER="ng_dev"  -e  MYSQL_PASSWORD="ng_dev"  创建用户并指定密码
    - -v  /home/nisp/mysql/data:/var/lib/mysql 映射mysql文件存储的目录
    - -v $PWD/my.cnf:/etc/mysql/my.cnf 指定mysql运行的定制配置(可以根据机器的性能适当调整)
      docker run --name master -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_USER="ng_dev" 
      -e MYSQL_PASSWORD="ng_dev" -v /home/nisp/mysql/data:/var/lib/mysql -v $PWD/my.cnf:/etc/mysql/my.cnf  -d yidun/mysql:5.6
- 进入容器
      docker exec -it master /bin/bash
- 进入到mysql终端
      mysql -uroot -p123456 
- 授权从库所使用的账号
      GRANT REPLICATION SLAVE ON *.* TO 'slave-user'@'主机B的IP' IDENTIFIED BY 'slave-password';//指定ip
      或者
      GRANT REPLICATION SLAVE ON *.* to 'calve-user'@'%' identified by 'slave-password'; //所有ip
- 查看主库状态
      show master status;//记录File 的值和Position的值
- 建立从库的my.cnf
          [client]
          port                        = 3306
          socket                      = /var/run/mysqld/mysqld.sock
      
          [mysqld_safe]
          pid-file                    = /var/run/mysqld/mysqld.pid
          socket                      = /var/run/mysqld/mysqld.sock
          nice                        = 0
      
          [mysqld]
          server-id                   = 1
          bind-address                = 0.0.0.0
          port                        = 3306
          user                        = mysql
          pid-file                    = /var/run/mysqld/mysqld.pid
          socket                      = /var/run/mysqld/mysqld.sock
          basedir                     = /usr
          datadir                     = /var/lib/mysql
          innodb_data_home_dir        = /var/lib/mysql
          innodb_log_group_home_dir   = /var/lib/mysql
          tmpdir                      = /tmp
          log-error                   = /var/lib/mysql/mysqld.log
          slow_query_log_file         = /var/lib/mysql/mysql-slow.log
          log-bin                     = mysql-bin
          lc_messages_dir             = /usr/share/mysql
          
          show_compatibility_56  =  1
          table_definition_cache  = 2048
          table_open_cache        = 2048
          innodb_open_files       = 4096
          slow_query_log=1
          long_query_time         = 0.1
          max_connections         = 6000
          query_cache_type        = 0
          character-set-server    = utf8mb4
          default-storage-engine  = innodb
          skip-external-locking
          expire_logs_days        = 7
          max_binlog_size         = 500M
          max_allowed_packet      = 16M
          
          innodb_adaptive_hash_index = 0
          
          skip-name-resolve
          max_connect_errors      = 10000
          
          innodb_buffer_pool_size = 4G
          innodb_buffer_pool_instances=4
          
          innodb_data_file_path   = ibdata1:512M:autoextend
          innodb_autoextend_increment     = 128
          innodb_log_files_in_group       = 2
          innodb_log_file_size    = 2G
          innodb_lock_wait_timeout        = 5
          innodb_flush_method     = O_DIRECT
          innodb_file_per_table      = 1
          
          innodb_flush_log_at_trx_commit    = 1
          sync_binlog    = 1
          innodb_thread_concurrency = 0
          innodb_io_capacity = 8000
          innodb_file_format         = Barracuda
          innodb_file_format_max = Barracuda
          innodb_change_buffering    = all
          innodb_buffer_pool_dump_pct=40
          innodb_buffer_pool_chunk_size=67108864
          innodb_print_all_deadlocks=1
          log_bin_use_v1_row_events = 1
          binlog_format = ROW
          binlog_cache_size = 65536
          relay-log-info-repository=TABLE
          master-info-repository=TABLE
          relay_log_recovery=ON
          slave-parallel-type=DATABASE
          slave_parallel_workers=16
          binlog_gtid_simple_recovery=TRUE
          enforce_gtid_consistency=ON
          gtid_mode=ON
          
          performance-schema-instrument='memory/%=COUNTED'
          performance_schema=1
          [mysql]
          default-character-set = utf8mb4
          !includedir /etc/mysql/conf.d/
          [client]
          port    = 3306
          socket   = /var/run/mysqld/mysqld.sock
          [mysqld_safe]
          pid-file  = /var/run/mysqld/mysqld.pid
          socket   = /var/run/mysqld/mysqld.sock
          nice    = 0
          [mysqld]
          user    = mysql
          pid-file  = /var/run/mysqld/mysqld.pid
          socket   = /var/run/mysqld/mysqld.sock
          port    = 3306
          basedir   = /usr
          datadir   = /var/lib/mysql
          tmpdir   = /tmp
          lc-messages-dir = /usr/share/mysql
          explicit_defaults_for_timestamp
          log-bin = mysql-bin
          server-id = 2 
          # Instead of skip-networking the default is now to listen only on
          # localhost which is more compatible and is not less secure.
          #bind-address  = 127.0.0.1
          #log-error = /var/log/mysql/error.log
          # Recommended in standard MySQL setup
          sql_mode=NO_ENGINE_SUBSTITUTION,STRICT_TRANS_TABLES
          symbolic-links=0
          !includedir /etc/mysql/conf.d/
- 运行从库容器
      docker run --name slave -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_USER="ng_dev" 
      -e MYSQL_PASSWORD="ng_dev" -v /home/nisp/mysql/data:/var/lib/mysql -v $PWD/my.cnf:/etc/mysql/my.cnf  -d yidun/mysql:5.6
- 进入从库容器
      docker exec -it slave /bin/bash
- 进入到mysql终端
      mysql -uroot -p123456
- 建立主库连接
      change master to
      master_host='主机A的IP',
      master_user='slave-user',//主机授权给从库的用户
      master_log_file='mysql-bin.000004',//主机File 的值
      master_log_pos=334,//主机Position的值
      master_port=3307,//主机指定的ip
      master_password='slave-password';//主机授权给从库的密码
- 启动从库
      start slave;
- 查看从库状态
      show slave status\G;// Slave_IO_Running: Yes ; Slave_SQL_Running: Yes 时表示成功连接


## 运行docker容器
``` 
docker run -d -it -p 8082:8080 --add-host myhbase:10.122.167.180 guardian-admin:v1.1

docker run -d -it guardian-stat:v1.1

正常build完镜像之后，通过tag命令重新打标签

docker tag image:tag privateRegister:port/image:tag
传到私有仓库：

docker push privateRegister:port/image:tag
在其他机器上通过拉取镜像:

docker pull privateRegister:port/image:tag
```

  
  
