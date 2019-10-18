#Nacos 安装

```bash
    docker run -d --name nacos -p 8848:8848 -v /etc/localtime:/etc/localtime:ro -e PREFER_HOST_MODE=hostname -e MODE=standalone nacos/nacos-server
```