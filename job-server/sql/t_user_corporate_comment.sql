DROP TABLE IF EXISTS `t_user_corporate_comment`;

CREATE TABLE `t_user_corporate_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `company_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '公司ID',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '笔记',
  `del_flag` tinyint NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户笔记';
