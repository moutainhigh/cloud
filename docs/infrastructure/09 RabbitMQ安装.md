#RabbitMQ 安装

##Docker 安装
```bash
    docker run --name rabbitmq -p 5671:5671 -p 5672:5672  -p 15672:15672 -p 15671:15671  -p 25672:25672 -v /etc/localtime:/etc/localtime:ro -v /opt/docker/rabbitmq/lib:/var/rabbitmq/lib -d rabbitmq:3.7-management
``` 

##连接
```
host:5672
```
##访问管理端
```
hsot:15672   guest/guest
```