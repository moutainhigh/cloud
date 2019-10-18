#MySQL 安装

##Docker 安装
```bash
    docker run --name mysql -p 3306:3306 -v /etc/localtime:/etc/localtime:ro -v /opt/docker/mysql/conf:/etc/mysql/conf.d -v /opt/docker/mysql/data:/var/lib/mysql -v /opt/docker/mysql/logs:/logs -e MYSQL_ROOT_PASSWORD=123456 -d mysql:5.7 --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci
```

##通过源安装
```
1、查看是否已有MySQL，并卸载
yum list installed | grep mysql
如有执行类似命令：yum -y remove mysql-community-client.x86_64
rpm -qa | grep -i mysql
find / -name mysql
如有执行类似命令：rm -rf /usr/share/mysql /usr/lib64/mysql /etc/selinux/targeted/active/modules/100/mysql
rm -rf /etc/my.cnf
vipw 输入 dd 删除
vipw -s 输入 dd 删除，wq!保存退出

2、下载安装文件
wget http://repo.mysql.com/mysql80-community-release-el7-3.noarch.rpm
rpm -ivh mysql80-community-release-el7-3.noarch.rpm
yum clean all
yum makecache

3、查看 yum 源仓库mysql 版本
yum repolist all | grep mysql
禁用：yum-config-manager --disable mysql80-community
启用：yum-config-manager --enable mysql57-community

4、安装（耗时较长）
yum install -y mysql-community-server

5、修改密码
开启服务：systemctl start mysqld.service
开机启动：systemctl enable mysqld.service
获取初始密码：
cat /var/log/mysqld.log | grep password
设置密码强弱：
mysql -u root -p
set global validate_password.policy=0;
set global validate_password.length=6;
首次设置root密码：
ALTER USER 'root'@'localhost' IDENTIFIED BY 'MySQL321456@';
flush privileges;

use mysql;
允许任何主机访问数据库：
update user set host='%' where user ='root';
允许远程客户端连接：
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'MySQL321456@';
flush privileges;

7、重置 root 密码
vim /etc/my.cnf
skip_grant_tables
systemctl restart mysqld.service
mysql -uroot -p
use mysql;
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'MySQL321456@';
flush privileges;

注释掉：skip_grant_tables
```