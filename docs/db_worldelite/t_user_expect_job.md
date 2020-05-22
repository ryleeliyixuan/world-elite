# 求职意向职位(t_user_expect_job)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) | MUL | NO |  | 用户ID |
| category_id | int(11) |  | NO |  | 职位 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |