# 字典表(t_dict)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| name | varchar(40) | MUL | NO |  | 名称 |
| type | int(11) |  | NO |  | 类型 |
| value | varchar(50) |  | NO |  | 值 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| del_flag | tinyint(4) |  | NO |  | 删除标志位 |