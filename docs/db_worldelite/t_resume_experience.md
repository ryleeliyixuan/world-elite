# 工作经验(t_resume_experience)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| resume_id | bigint(20) |  | NO |  | 简历ID |
| start_time | timestamp |  | YES |  | 开始时间 |
| finish_time | timestamp |  | YES |  | 结束时间 |
| on_work | tinyint(4) |  | NO |  | 在职 |
| company | varchar(100) |  | NO |  | 公司 |
| depart | varchar(50) |  | NO |  |  |
| post | varchar(50) |  | NO |  | 职位 |
| description | text |  | YES |  | 描述 |
| position | int(11) |  | NO |  | 排序位 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |