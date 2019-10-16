#------------------------------------------------
#Dockerfile内容：
#FROM       指明当前镜像继承的基镜像,编译当前镜像时候会自动下载基镜像
#MAINTAINER 当前镜像的作者和邮箱,使用空格隔开
#VOLUME     挂载目录
#ARG        定义参数
#COPY       从当前工作目录复制文件到镜像目录中并重新命名
#RUN        在当前镜像上执行Linux命令
#EXPOSE     监听的端口号
#ENTRYPOINT 让容器像一个可执行程序一样运行
#------------------------------------------------
FROM openjdk:8-jdk-alpine
MAINTAINER yt cloud@smart4y.com
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
#RUN bash -c 'touch /app.jar'
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' > /etc/timezone
EXPOSE 7211
ENV JAVA_OPTS="-DXms128m -DXmx512m"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]