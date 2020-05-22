# 职位表(t_job)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | bigint(20) | PRI | NO |  |  |
| company_id | bigint(20) |  | NO |  | 公司ID |
| creator_id | bigint(20) |  | NO |  | 职位创建者 |
| job_type | int(11) |  | NO |  | 招聘类型 |
| name | varchar(40) |  | NO |  | 名字 |
| category_id | int(11) |  | NO |  | 类型 |
| depart | varchar(45) |  | NO |  | 所属部门 |
| description | text |  | NO |  | 职位描述 |
| min_degree_id | int(11) |  | NO |  | 最低学历 |
| min_salary | int(11) |  | NO |  | 最低薪酬 |
| max_salary | int(11) |  | NO |  | 最高薪酬 |
| salary_months | int(11) |  | YES |  | 薪资发放月数 |
| city_id | int(11) |  | NO |  | 工作城市 |
| address_id | int(11) |  | NO |  | 工作地点 |
| address | varchar(255) |  | NO |  | 工作地址 |
| status | tinyint(4) |  | NO |  | 职位状态 |
| pub_time | timestamp |  | NO |  | 发布时间 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| del_flag | tinyint(4) |  | NO |  |  |
| remark | varchar(255) |  | NO |  | 备注 |