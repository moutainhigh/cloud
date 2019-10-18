#Java 环境安装

若通过 Docker 部署，则不需要单独在安装 JDK。

##Java 运行环境安装
```bash
#1、下载jdk1.8并检查安装包大小是否符合
#oracle账号：youtao531@163.com Oracle05995312439

#2、解压至安装目录
mkdir /usr/local/java
tar -zxvf /opt/tools/jdk-8u221-linux-x64.tar.gz -C /usr/local/java/

#3、设置环境变量
vim /etc/profile
export JAVA_HOME=/usr/local/java/jdk1.8.0_221
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH

#4、使环境变量生效
source /etc/profile

#5、添加软链接、检查安装
ln -s /usr/local/java/jdk1.8.0_221/bin/java /usr/bin/java
java -version
```
