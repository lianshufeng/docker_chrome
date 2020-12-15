
# 编译jar包
FROM lianshufeng/maven as builder
ARG FILE_NAME="capture-0.0.1-SNAPSHOT.jar"
COPY ./src /tmp/capture/src
COPY ./pom.xml /tmp/capture/pom.xml
RUN set -xe \
	&& mkdir -p /opt/capture/ \
	&& sh /mvn_package.sh /tmp/capture/ target/$FILE_NAME /opt/capture/capture.jar


# 运行环境
FROM lianshufeng/jdk
COPY --from=builder /opt/capture/capture.jar /opt/capture/capture.jar

ARG Fonts_URL="http://build.dzurl.top/Fonts.zip"
ARG Chrome_URL="https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm"

#安装工具库
RUN yum install unzip wget curl fontconfig -y

#下载字体
RUN wget -O /tmp/Fonts_URL.zip --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" $Fonts_URL


#安装字库
RUN set -xe \
	&& unzip -o -d /usr/share/fonts/ /tmp/Fonts_URL.zip \
	&& rm -rf /tmp/Fonts_URL.zip

#安装chrome
RUN yum install -y $Chrome_URL

#安装依赖
RUN yum install -y libappindicator-gtk3 liberation-fonts


#设置环境变量
ENV CHROME_HOME "/opt/google/chrome"
ENV PATH $CHROME_HOME:$PATH


#启动
ENV ENTRYPOINT="nohup java -Dfile.encoding=UTF-8 -Xmx600m -Xms300m -Duser.timezone=GMT+8 -jar /opt/capture/capture.jar"
#创建启动脚本
RUN set -xe \
	#引导程序
	& echo "#!/bin/bash" > /opt/bootstrap.sh \
	&& echo "source /etc/profile" >> /opt/bootstrap.sh \
	&& echo "export LANG=en_US.UTF-8" >> /opt/bootstrap.sh \
	&& echo "echo \${ENTRYPOINT}|awk '{run=\$0;system(run)}'" >> /opt/bootstrap.sh 


