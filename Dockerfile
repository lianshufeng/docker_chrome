FROM selenium/standalone-chrome:106.0



#切换用户
USER seluser
ENTRYPOINT ["/opt/bin/entry_point.sh"]



