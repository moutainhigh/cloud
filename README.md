#cloud

打包工程的电脑配置环境变量，添加DOCKER_HOST，值为tcp://host:2375

使用maven编译打包镜像
打开cmd窗口，确定环境变量配置生效：输入 echo %DOCKER_HOST%，会输出 tcp://192.168.11.88:2375
然后使用命令 mvn clean package dockerfile:build -Dmaven.test.skip=true 编译项目并构建docker镜像，编译结束自动推送镜像到docker主机中。

##推送到阿里云私服并拉取到阿里云服务器
```sh
docker login --username=youtao531@163.com registry.cn-hangzhou.aliyuncs.com

docker tag cloud/cloud-hippo:0.9.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:0.9.0
docker tag cloud/cloud-owl:0.9.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:0.9.0
docker tag cloud/cloud-spider:0.9.0 registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:0.9.0

docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:0.9.0
docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:0.9.0
docker push registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:0.9.0

docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-hippo:0.9.0
docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-owl:0.9.0
docker pull registry.cn-hangzhou.aliyuncs.com/smart4y/cloud-spider:0.9.0
```