# 推荐表(t_recommend)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| object_id | bigint(20) |  | NO |  | 对象ID |
| object_type | tinyint(4) |  | NO |  | 对象类型 |
| position | int(11) |  | NO |  | 排序 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |