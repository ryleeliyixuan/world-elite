# 实践经验(t_resume_practice)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| resume_id | bigint(20) |  | NO |  | 简历ID |
| start_time | timestamp |  | YES |  | 开始时间 |
| finish_time | timestamp |  | YES |  | 结束时间 |
| title | varchar(50) |  | NO |  | 标题 |
| post | varchar(50) |  | NO |  | 职位 |
| description | text |  | YES |  | 描述 |
| position | int(11) |  | NO |  | 排序 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| on_work | tinyint(4) |  | YES |  | 实践中 |