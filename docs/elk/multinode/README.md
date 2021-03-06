## 用法
- 本地启动整个ELK：`./start.sh`,会启动2个ES节点(es1/es2)，其中只有es1能够接收外部请求
- 启动之后检查ES集群状态：`curl localhost:9200/_cluster/health`
- 查看节点情况：`curl localhost:9200/_cat/nodes`
- 启动之后可以访问Kibana：[http://localhsot:5601](http://localhsot:5601)
- 首次启动Kibana需要创建Index(需要ES中有Index数据之后才行)，Logstash默认的Index为`logstash-*`格式。
- 关闭ELK： `./stop.sh`，将清空所有数据
- 用2种方式打日志：通过gelf和通过redis
- Redis默认密码：`123456`


## redis日志测试
- 保证[http://localhsot:5601](http://localhsot:5601)可以正常访问
- 登录Redis： `redis-cli -h localhost -p 6379 -a 123456`
- 在Redis中随便打点内容(需要json格式)：`lpush redis-log '{"msg":"hello world"}'`
- 在Kibana上创建名为`logstash-*`的Index
- 在Kibana上切换到`discovery`页面便可以看到`hello world`了。


## 端口映射
|宿主机端口|Docker容器|Docker容器端口|
| --- | --- | --- |
|9202|es1|9200|
|9302|es1|9300|
|12201|logstash|12201(udp)|
|5601|kibana|5601|
|6379|redis|6379|
