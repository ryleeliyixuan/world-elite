# 简历投递表(t_job_apply)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| job_id | bigint(20) |  | NO |  | 职位ID |
| user_id | bigint(20) |  | NO |  | 投递用户 |
| resume_id | bigint(20) |  | NO |  | 投递简历 |
| status | tinyint(4) |  | NO |  | 投递状态 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |