# 企业表(t_company)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | bigint(20) | PRI | NO |  |  |
| name | varchar(50) |  | NO |  | 简称 |
| full_name | varchar(150) | UNI | NO |  | 全称 |
| logo | varchar(100) |  | NO |  | logo url |
| scale_id | int(11) |  | NO |  | 公司规模 |
| stage_id | int(11) |  | NO |  | 公司阶段 |
| industry_id | int(11) |  | NO |  | 所属行业 |
| property_id | int(11) |  | NO |  | 企业性质 |
| stock_num | varchar(20) |  | NO |  | 股票代码 |
| homepage | varchar(100) |  | NO |  | 主页 |
| synopsis | varchar(150) |  | NO |  | 一句话介绍 |
| introduction | text |  | YES |  | 公司介绍 |
| regist_addr | varchar(150) |  | NO |  | 注册地址 |
| industry | varchar(45) |  | NO |  | 所属行业 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |
| del_flag | tinyint(4) |  | NO |  | 删除标志位 |