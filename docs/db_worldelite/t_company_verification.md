# 公司验证材料(t_company_verification)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) | UNI | NO |  | 用户ID |
| id_card | varchar(45) |  | NO |  | 身份证 |
| id_card_pic | varchar(100) |  | NO |  | 身份证正面 |
| real_name | varchar(45) |  | NO |  | 真实姓名 |
| company | varchar(100) |  | NO |  | 公司名 |
| post | varchar(45) |  | NO |  | 职位 |
| company_license_pic | varchar(100) |  | NO |  | 公司营业执照 |
| status | tinyint(4) |  | NO |  | 状态位 |
| remark | varchar(255) |  | NO |  | 备注 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |