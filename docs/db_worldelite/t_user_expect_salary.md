# 求职意向薪资(t_user_expect_salary)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) | UNI | NO |  | 用户ID |
| min_value | int(11) |  | NO |  | 最低薪资 |
| max_value | int(11) |  | NO |  | 最高薪资 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |