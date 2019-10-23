
SOURCE_VERSION=$0
TARGET_VERSION=$1
ALIYUN_REGISTRY=registry.cn-hangzhou.aliyuncs.com

echo "#1 开始编译打包并构建服务镜像"
echo "mvn clean install package..."
mvn clean install package

echo "#2 推送服务镜像到阿里云私服"
docker tag cloud/cloud-hippo:${SOURCE_VERSION} ${ALIYUN_REGISTRY}/smart4y/cloud-hippo:${TARGET_VERSION}
docker push ${ALIYUN_REGISTRY}/smart4y/cloud-hippo:${TARGET_VERSION}

docker tag cloud/cloud-owl:${SOURCE_VERSION} ${ALIYUN_REGISTRY}/smart4y/cloud-owl:${TARGET_VERSION}
docker push ${ALIYUN_REGISTRY}/smart4y/cloud-owl:${TARGET_VERSION}

docker tag cloud/cloud-spider:${SOURCE_VERSION} ${ALIYUN_REGISTRY}/smart4y/cloud-spider:${TARGET_VERSION}
docker push ${ALIYUN_REGISTRY}/smart4y/cloud-spider:${TARGET_VERSION}