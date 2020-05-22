# 收藏表(t_favorite)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) |  | NO |  | 用户ID |
| object_id | bigint(20) |  | NO |  | 对象ID |
| type | tinyint(4) |  | NO |  | 对象类型 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |