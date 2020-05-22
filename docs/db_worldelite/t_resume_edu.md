# 简历教育信息表(t_resume_edu)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| resume_id | bigint(20) |  | NO |  | 对应简历 |
| user_id | bigint(20) |  | NO |  | 用户ID |
| school_id | int(11) |  | NO |  | 学校Id |
| school_name | varchar(100) |  | NO |  | 学校名 |
| major_id | int(11) |  | NO |  | 专业ID |
| major_name | varchar(100) |  | NO |  | 专业名 |
| degree_id | int(11) |  | NO |  | 学历ID |
| start_time | timestamp |  | YES |  | 开始时间 |
| finish_time | timestamp |  | YES |  | 结束时间 |
| gpa | double |  | NO |  | 绩点 |
| position | int(11) |  | NO |  | 排序位 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |