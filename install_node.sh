#!/bin/bash

nodejs_version=v10.16.3
nodejs_file=node-${nodejs_version}-linux-x64
nodejs_path=/opt
nodejs_home=${nodejs_path}/${nodejs_file}

url=https://nodejs.org/dist/${nodejs_version}/${nodejs_file}.tar.xz
echo url : $url 
#下载
curl $url -O


#解压到目标目录
mkdir -p ${nodejs_home}
tar -xf ${nodejs_file}.tar.xz -C ${nodejs_path} 

#软连接
rm -rf /usr/bin/node /usr/bin/npm /usr/bin/npx

ln -s ${nodejs_home}/bin/node /usr/bin/node
ln -s ${nodejs_home}/bin/npm /usr/bin/npm
ln -s ${nodejs_home}/bin/npx /usr/bin/npx

#删除安装文件
rm -rf ${nodejs_file}.tar.xz

