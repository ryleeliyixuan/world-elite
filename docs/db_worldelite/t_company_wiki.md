# 企业百科(t_company_wiki)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| company_id | bigint(20) |  | NO |  | 公司ID |
| summary | varchar(500) |  | NO |  | 摘要 |
| content | longtext |  | YES |  | 内容 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |