# 公司地址(t_company_address)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| company_id | bigint(20) |  | NO |  | 公司ID |
| address | varchar(100) |  | NO |  | 公司地址 |
| city_id | int(11) |  | NO |  | 所在城市 |
| latitude | double |  | NO |  | 纬度 |
| longitude | double |  | NO |  | 经度 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |