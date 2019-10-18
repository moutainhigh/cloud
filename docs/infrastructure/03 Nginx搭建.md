#Nginx 搭建

一般有两种方式搭建，一种是直接安装在阿里云服务器上，另一种为安装在 Docker 容器中

##通过源安装
```shell script
    echo '下载源安装（配置文件路径/etc/nginx）'
    rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
    yum install -y nginx
    systemctl start nginx.service
    systemctl enable nginx.service
```

##Docker安装
```shell script
    echo '下载镜像安装（配置文件路径/opt/docker/nginx/）'
    docker run --name nginx -p 80:80 -v /etc/localtime:/etc/localtime:ro -v /opt/docker/nginx/logs:/var/log/nginx -v /opt/docker/nginx/html:/usr/share/nginx/html -v /opt/docker/nginx/nginx.conf:/etc/nginx/nginx.conf:ro -v /opt/docker/nginx/conf.d:/etc/nginx/conf.d -d nginx:latest
    echo '若执行以上命令报错，则依次执行以下命令后重试（重建docker0网络恢复）'
    echo 'pkill docker' 
    echo 'iptables -t nat -F'
    echo 'ifconfig docker0 down' 
    echo 'brctl delbr docker0' 
    echo 'docker -d' 
    echo 'service docker restart'
    echo 'Nginx Reload: docker exec -it nginx /etc/init.d/nginx reload'
```

##nginx配置
- [配置文件目录](nginx)
- [阿里云免费证书](nginx/cert)