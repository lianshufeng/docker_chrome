FROM selenium/standalone-chrome:106.0


#修改默认端口
#ENV SE_OPTS="--docker-port 9000"

USER root
RUN apt-get update 
RUN apt-get install redir 
RUN sed -i '1i\redir :9000 :4444' /opt/bin/start-selenium-standalone.sh

expose 9000

#切换用户
USER seluser
ENTRYPOINT ["/opt/bin/entry_point.sh"]



