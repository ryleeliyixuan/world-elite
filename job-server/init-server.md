#!/bin/sh

# 创建一个sudo账户
adduser webuser
usermod -aG sudo webuser
su webuser

# 禁用root用户
sudo passwd -l root

# 安装 git
sudo apt-get install git

# 安装jdk
sudo apt-get update
sudo apt-get install openjdk-8-jdk
java -version

# 安装nginx
sudo apt-get install nginx

# 安装redis
sudo apt-get install redis-server

# 安装npm
curl -o- https://raw.githubusercontent.com/creationix/nvm/v0.33.11/install.sh | bash
nvm install v11.0.0
npm config set registry https://registry.npm.taobao.org
npm config list

# 创建相关目录，设置相关权限
sudo mkdir /apps/
sudo mkdir /apps/worldelite
sudo mkdir /apps/worldelite/temp
sudo mkdir /apps/worldelite/logs
sudo mkdir /apps/worldelite/search-index
sudo mkdir /apps/worldelite/static-admin
sudo mkdir /apps/worldelite/static-web
sudo mkdir /apps/worldelite/static-company

mkdir ~/source  #源码目录
mkdir ~/apps    #程序目录

# 安装 wkhtmltopdf


# 下载源码，构建jar包，启动服务

# 配置服务和开机启动

sudo vi /etc/rc.local

添加:

```
/etc/init.d/eliteApiServer start
```