# 下载任务表(t_download)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| name | varchar(100) |  | NO |  | 名称 |
| user_id | bigint(20) |  | NO |  | 任务创建者 |
| url | varchar(100) |  | NO |  | url |
| progress | int(11) |  | NO |  | 处理进度 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |