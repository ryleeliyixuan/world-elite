DROP TABLE IF EXISTS `t_config_module`;
CREATE TABLE `t_config_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) DEFAULT NULL COMMENT '模块名',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '模板类型',
  `config_type` tinyint(4) DEFAULT NULL COMMENT '配置类型',
  `value` tinyint(4) NOT NULL DEFAULT '0' COMMENT '配置值',
  `position` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='首页配置模块表';

-- 初始数据
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(1, '用户端设置', 1, 8, 1, 1, '2021-01-27 20:59:43.0', '2021-01-27 21:05:05.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(2, '企业端设置', 1, 9, 1, 2, '2021-01-27 20:59:56.0', '2021-01-28 14:04:28.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(3, '轮播图设置', 2, 10, 1, 1, '2021-01-27 21:03:02.0', '2021-01-28 14:04:29.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(4, '推荐板块设置', 2, 11, 1, 2, '2021-01-27 21:03:02.0', '2021-01-28 14:04:29.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(5, '推荐职位设置', 2, 12, 1, 3, '2021-01-27 21:03:03.0', '2021-01-28 14:04:29.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(6, '推荐百科设置', 2, 13, 1, 4, '2021-01-27 21:03:03.0', '2021-01-28 14:04:29.0');
INSERT INTO db_worldelite_activity.t_config_module
(id, name, `type`, config_type, value, `position`, create_time, update_time)
VALUES(7, '推荐mock设置', 2, 14, 1, 5, '2021-01-27 21:03:03.0', '2021-01-28 14:04:29.0');
