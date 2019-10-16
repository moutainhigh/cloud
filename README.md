#cloud

打包工程的电脑配置环境变量，添加DOCKER_HOST，值为tcp://host:2375

使用maven编译打包镜像
打开cmd窗口，确定环境变量配置生效：输入 echo %DOCKER_HOST%，会输出 tcp://192.168.11.88:2375
然后使用命令 mvn clean package dockerfile:build -Dmaven.test.skip=true 编译项目并构建docker镜像，编译结束自动推送镜像到docker主机中。