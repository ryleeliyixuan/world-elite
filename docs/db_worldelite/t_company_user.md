# 企业用户(t_company_user)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) | UNI | NO |  | 用户ID |
| company_id | bigint(20) |  | NO |  | 公司ID |
| depart | varchar(45) |  | NO |  | 部门 |
| post | varchar(45) |  | NO |  | 岗位 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |