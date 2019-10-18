#SSR 搭建

##SSR Client
###下载客户端
```shell script
    yum install python-pip
    pip install --upgrade pip
    pip install shadowsocks
```
###配置
```bash
    mkdir /etc/shadowsocks
    #server填写你代理服务器的IP
    #server_port代理服务器设置的端口
    #password代理服务器密码
    vim /etc/shadowsocks/shadowsocks.json
        {
            "server":"69.17.66.77",
            "server_port":6666,
            "local_address": "127.0.0.1",
            "local_port":1080,
            "password":"password",
            "timeout":300,
            "method":"aes-256-cfb",
            "fast_open": false,
            "workers": 1
        }
```
###配置自启动
```bash
    vi /etc/systemd/system/shadowsocks.service
    #内容
        [Unit]
        Description=Shadowsocks
        [Service]
        TimeoutStartSec=0
        ExecStart=/usr/bin/sslocal -c /etc/shadowsocks/shadowsocks.json
        [Install]
        WantedBy=multi-user.target
    #开机启动
    systemctl enable shadowsocks.service
    systemctl start shadowsocks.service
    systemctl status shadowsocks
```
###其他配置
```bash
#Shadowsocks是一个socket5服务，我们需要使用privoxy把流量转到http/https上
    yum install privoxy
    systemctl enable privoxy
    systemctl start privoxy
    systemctl status privoxy
    
    #添加  
    vim /etc/privoxy/config
    listen-address 127.0.0.1:8118
    forward-socks5t / 127.0.0.1:1080 .
    
    #修改
    vim /etc/profile
    export http_proxy=http://127.0.0.1:8118
    export https_proxy=http://127.0.0.1:8118

    #刷新
    source /etc/profile
    systemctl start shadowsocks
    systemctl start privoxy
``` 