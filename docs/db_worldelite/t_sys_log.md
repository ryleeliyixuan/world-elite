# 系统日志表(t_sys_log)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) |  | NO |  | 用户ID |
| user_ip | varchar(16) |  | NO |  | IP |
| req_method | varchar(100) |  | NO |  | 调用方法 |
| req_desc | text |  | YES |  | 请求信息 |
| create_time | timestamp |  | NO |  |  |