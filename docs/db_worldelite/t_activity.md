# 活动表(t_activity)
| 列名 | 类型 | KEY | 可否为空 | 默认值 | 注释 |
| ---- | ---- | ---- | ---- | ---- | ----  |
| id | int(11) | PRI | NO |  |  |
| user_id | bigint(20) |  | NO |  | 活动发布者 |
| city_id | int(11) |  | NO |  | 所在城市 |
| title | varchar(100) |  | NO |  | 标题 |
| thumbnail | varchar(100) |  | NO |  | 图片缩略图 |
| summary | varchar(512) |  | NO |  | 摘要 |
| url | varchar(45) |  | NO |  | 链接 |
| description | longtext |  | YES |  | 活动详情 |
| status | tinyint(4) |  | NO |  | 状态 |
| start_time | timestamp |  | YES |  | 开始时间 |
| finish_time | timestamp |  | YES |  | 结束时间 |
| address | varchar(100) |  | NO |  | 活动地址 |
| create_time | timestamp |  | NO |  |  |
| update_time | timestamp |  | NO |  |  |