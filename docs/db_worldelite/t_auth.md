# 第三方登录(t_auth)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  | id |
| user_id | bigint(20) |  | NO |  | 用户ID |
| auth_type | tinyint(4) |  | NO |  | 平台类型 |
| auth_id | varchar(64) |  | NO |  | 唯一ID |
| open_id | varchar(50) |  | NO |  | openID |
| verified | tinyint(4) |  | NO |  | 是否绑定 |
| expire_time | bigint(20) |  | NO |  | 过期时间 |
| create_time | timestamp |  | NO |  | 创建时间 |
| update_time | timestamp |  | NO |  | 更新时间 |