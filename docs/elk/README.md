# ELK搭建
- standalone    单机单节点（即一个host一个node）
- multinode     单机多节点（即一个host多个node）
- local 主要包含本地开发过程所需要用到的基础设施，比如RabbitMQ和ELK等，均通过Docker在本地机器启动。
- remote 主要用于生产环境所需的基础设施，主要针对"虚拟机+Docker"的部署场景，本地使用Vagrant虚拟机。
