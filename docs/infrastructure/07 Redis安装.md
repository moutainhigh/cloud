#Redis 安装

##Docker安装
```bash
    docker run --name redis -p 16379:6379 -v /etc/localtime:/etc/localtime:ro -v /opt/docker/redis/data:/data -d redis:3.2 redis-server --appendonly yes --requirepass "123456"
```

##通过源安装
```
1、下载源安装
yum install -y http://rpms.famillecollet.com/enterprise/remi-release-7.rpm
yum --enablerepo=remi install -y redis
systemctl start redis.service
systemctl enable redis.service

2、修改配置
vim /etc/redis.conf
开启守护进程模式：
daemonize no --> daemonize yes 
允许远程访问（两种方式）
- 关闭保护模式：protected-mode yes --> protected-mode no 
- 设置外网方式：bind 0.0.0.0
设置密码授权：
# requirepass foodbared --> requirepass 123456

连接命令：./redis-cli -a 123456
```