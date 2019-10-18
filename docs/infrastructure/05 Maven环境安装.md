#Maven 环境安装

```
1、软件下载
wget http://mirrors.gigenet.com/apache/maven/maven-3/3.6.2/binaries/apache-maven-3.6.2-bin.tar.gz

2、解压至安装目录
mkdir /usr/local/maven
tar -zxvf /opt/tools/apache-maven-3.6.2-bin.tar.gz -C /usr/local/maven/

3、设置环境变量
vim /etc/profile
export MAVEN_HOME=/usr/local/maven/apache-maven-3.6.2
export PATH=${MAVEN_HOME}/bin:$PATH

4、使环境变量生效
source /etc/profile

5、检查安装
mvn -v
```