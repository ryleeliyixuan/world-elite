# 消息通知表(t_message)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| from_user | bigint(20) |  | NO |  | 发送用户 |
| to_user | bigint(20) |  | NO |  | 接收用户 |
| content | varchar(255) |  | NO |  | 内容 |
| url | varchar(100) |  | NO |  | 链接 |
| read_flag | tinyint(4) |  | NO |  | 是否已读 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |