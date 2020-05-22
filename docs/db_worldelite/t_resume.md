# 简历表(t_resume)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | bigint(20) | PRI | NO |  |  |
| user_id | bigint(20) |  | NO |  | 用户ID |
| name | varchar(100) |  | NO |  | 姓名 |
| birth | timestamp |  | YES |  | 生日 |
| gender | tinyint(4) |  | NO |  | 性别 |
| country_id | int(11) |  | NO |  | 国籍 |
| max_degree_id | int(11) |  | NO |  | 最高学历 |
| marital_status | tinyint(4) |  | NO |  | 婚姻状况 |
| return_time | timestamp |  | YES |  | 回国时间 |
| graduate_time | timestamp |  | YES |  | 毕业时间 |
| cur_place | varchar(100) |  | NO |  | 现居地址 |
| lang | tinyint(4) |  | NO |  | 语言 |
| attach_resume | varchar(100) |  | NO |  | 附件简历 |
| introduction | text |  | YES |  | 自我介绍 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |