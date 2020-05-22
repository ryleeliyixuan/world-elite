# 职位类型(t_job_category)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| parent_id | int(11) | MUL | NO |  | 父类别 |
| name | varchar(45) |  | NO |  | 名称 |
| position | int(11) |  | NO |  | 排序号 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| del_flag | tinyint(4) |  | NO |  |  |