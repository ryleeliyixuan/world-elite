@echo off
echo.
echo Begin to generate mapper.
echo.

cd %~dp0

java -jar mybatis-generator-core-1.3.7.jar  -configfile  mybatis-generator-config.xml  -overwrite

echo Finish to generate mapper.

pause