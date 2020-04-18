#!/bin/sh

module=eliteApiServer # 打包模块名
deployPath=~/apps # 服务器发布路径
sourcePath=~/source/world-elite/job-server #服务器源码路径
appPropsFile=src/main/resources/application.properties;

cd ${sourcePath}

#修改application.properties
sed -i 's/local/prod/g' ${appPropsFile}

# 构建jar包
sudo ./gradlew clean build -x test

# 拷贝到相应目录
sudo cp build/libs/*.jar ${deployPath}/${module}.jar
sudo cp GeoLite2-City.mmdb /apps/worldelite/GeoLite2-City.mmdb

# 重启服务
if [ ! -f "/etc/init.d/${module}" ];then
  sudo chmod +x ${deployPath}/${module}.jar
  sudo ln -s ${deployPath}/${module}.jar /etc/init.d/${module}
fi

sudo /etc/init.d/${module} restart

#恢复application.properties
sed -i 's/dev/local/g' ${appPropsFile}