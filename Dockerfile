#
# 安装： docker build -t lianshufeng/chrome   ./ 
# Debug: docker run -d -p 9222:9222 lianshufeng/chrome
# 截图 : 
#
#

FROM centos:8
MAINTAINER lianshufeng <251708339@qq.com>



ARG Fonts_URL="http://build.dzurl.top/Fonts.zip"
ARG Chrome_URL="http://build.dzurl.top/google-chrome-stable_current_x86_64.rpm"


#安装工具库
RUN yum install unzip wget curl fontconfig -y


#下载资源
RUN wget -O /tmp/google-chrome-stable_current_x86_64.rpm --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" $Chrome_URL
RUN wget -O /tmp/Fonts_URL.zip --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" $Fonts_URL

#安装字库
RUN unzip -o -d /usr/share/fonts/ /tmp/Fonts_URL.zip
RUN rm -rf /tmp/Fonts_URL.zip


#安装google
RUN yum install -y /tmp/google-chrome-stable_current_x86_64.rpm
RUN rm -rf /tmp/google-chrome-stable_current_x86_64.rpm


#安装依赖
RUN yum install -y libappindicator-gtk3 liberation-fonts




#设置环境变量
ENV CHROME_HOME "/opt/google/chrome"
ENV PATH $CHROME_HOME:$PATH




#暴露的端口,远程调试的端口
EXPOSE 9222 



