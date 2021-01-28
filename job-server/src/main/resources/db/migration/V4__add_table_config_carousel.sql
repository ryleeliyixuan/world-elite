DROP TABLE IF EXISTS `t_config_carousel`;
CREATE TABLE `t_config_carousel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(50) NOT NULL COMMENT '图片名',
  `url` varchar(100) DEFAULT NULL COMMENT '图片路径',
  `target` varchar(200) DEFAULT NULL COMMENT '跳转链接',
  `position` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='首页轮播图';