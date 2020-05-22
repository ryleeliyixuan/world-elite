# 用户表(t_user)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | bigint(20) | PRI | NO |  |  |
| name | varchar(50) |  | NO |  | 名称 |
| email | varchar(60) | UNI | NO |  | 用户邮箱 |
| phone_code | varchar(10) |  | NO |  | 国际区号 |
| phone | bigint(20) |  | NO |  | 电话 |
| avatar | varchar(100) |  | NO |  | 头像 |
| gender | tinyint(4) |  | NO |  | 性别 |
| type | tinyint(4) |  | NO |  | 用户类型 |
| status | tinyint(4) |  | NO |  | 状态 |
| password | varchar(256) |  | NO |  | 密码 |
| salt | varchar(50) |  | NO |  | 密码盐 |
| token | varchar(40) |  | NO |  | 登录token |
| expired | bigint(20) |  | NO |  | 过期时间 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| del_flag | tinyint(4) |  | NO |  |  |
| subscribe_flag | tinyint(4) |  | NO |  | 订阅标志位 |