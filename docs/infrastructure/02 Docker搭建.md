#Docker 搭建

##Docker
```shell script
echo '1、安装必要的一些系统工具'
yum install -y yum-utils device-mapper-persistent-data lvm2
echo '2、添加软件源信息'
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
echo '3、更新并安装 Docker-CE'
yum install -y docker-ce
echo '4、开启Docker服务'
systemctl start docker.service
systemctl enable docker.service
```

###Docker 环境变量设置
```bash
    #设置volume根路径，编辑环境变量配置文件
    vim ~/.bash_profile
    #添加以下环境变量
    export DOCKER_VOL_DIR=/opt/docker

    source ~/.bash_profile
```

###Docker 远程访问配置
```bash
    #1 编辑docker配置文件
    vim /lib/systemd/system/docker.service 
    #2 修改ExecStart内容
    ExecStart=/usr/bin/dockerd -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock
    #3 加载Docker守护线程
    systemctl daemon-reload
    #4 重启Docker
    systemctl restart docker
    #5 本地电脑上使用telnet进行测试2375端口是否开启成功
    telnet localhost 2375
    #6 阿里云ECS安全组开放2375端口
```

##Docker Compose
```bash
#curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
curl -L https://get.daocloud.io/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose
docker-compose --version


```


##登录阿里云私服
```shell script
docker login --username=youtao531@163.com registry.cn-hangzhou.aliyuncs.com
```
##镜像推送到阿里云私服
```shell script
docker login --username=youtao531@163.com registry.cn-hangzhou.aliyuncs.com

docker tag cloud/cloud-hippo:1.0.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:1.0.0

docker tag cloud/cloud-owl:1.0.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:1.0.0

docker tag cloud/cloud-spider:1.0.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:1.0.0
docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:1.0.0
pwd
```

##拉取阿里云镜像
```shell script
docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:1.0.0
docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:1.0.0
docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:1.0.0
pwd
```

##删除无tag镜像
```shell script
docker images|grep none|awk '{print $3}'|xargs docker rmi
```

##显示指定列
```shell script
docker ps -a --format "table {{.Names}}\t{{.Image}}\t{{.Status}}"
```
###支持的列名
```
--format="TEMPLATE"
Pretty-print containers using a Go template.
Valid placeholders:
.ID - Container ID
.Image - Image ID
.Command - Quoted command
.CreatedAt - Time when the container was created.
.RunningFor - Elapsed time since the container was started.
.Ports - Exposed ports.
.Status - Container status.
.Size - Container disk size.
.Names - Container names.
.Labels - All labels assigned to the container.
.Label - Value of a specific label for this container. For example {{.Label "com.docker.swarm.cpu"}}
.Mounts - Names of the volumes mounted in this container.
```