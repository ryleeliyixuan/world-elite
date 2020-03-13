#!/bin/sh
deployPath=/apps/worldelite/static-admin # 服务器发布路径
sourcePath=~/source/world-elite/job-admin #服务器源码路径

cd ${sourcePath}

# 安装依赖包,并打包
npm install
npm run build:stage

# 覆盖发布
sudo cp -Rf dist/* ${deployPath} 