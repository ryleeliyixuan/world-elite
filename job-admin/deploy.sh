#!/bin/sh

deployPath=/apps/bor/static # 服务器发布路径
sourcePath=~/source/bor-erp/erp-web #服务器源码路径

cd ${sourcePath}

# 安装依赖包,并打包
sudo npm install
sudo npm run build:prod

# 覆盖发布
sudo cp -Rf dist/* ${deployPath} 
