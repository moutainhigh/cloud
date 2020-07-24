
#源镜像版本
SOURCE_VERSION=$1
#目标镜像版本
TARGET_VERSION=$2
#阿里云私服地址
ALIYUN_REGISTRY=registry.cn-hangzhou.aliyuncs.com

#echo "#1 开始编译打包并构建服务镜像"
#echo "mvn clean package..."
#mvn clean package

echo "#2 推送服务镜像到阿里云私服 password:Docker123456"
docker login --username=youtao531@163.com ${ALIYUN_REGISTRY}
docker tag cloud/cloud-hippo:${SOURCE_VERSION} ${ALIYUN_REGISTRY}/smart4y/cloud-hippo:${TARGET_VERSION}
docker push ${ALIYUN_REGISTRY}/smart4y/cloud-hippo:${TARGET_VERSION}

echo "删除无效镜像"
docker images|grep none|awk '{print $3}'|xargs docker rmi