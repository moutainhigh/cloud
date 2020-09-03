
#源镜像版本
VERSION=$1
#阿里云私服地址
ALIYUN_REGISTRY=registry.cn-hangzhou.aliyuncs.com

echo "拉取阿里云私服镜像"
docker pull ${ALIYUN_REGISTRY}/smart4y/cloud-hippo:${VERSION}
docker pull ${ALIYUN_REGISTRY}/smart4y/cloud-owl:${VERSION}
docker pull ${ALIYUN_REGISTRY}/smart4y/cloud-spider:${VERSION}

echo "删除无效镜像"
docker images|grep none|awk '{print $3}'|xargs docker rmi

echo "运行镜像"
docker-compose -f docker-compose-aliyun.yml up -d