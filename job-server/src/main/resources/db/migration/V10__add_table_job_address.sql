-- 添加职位地址表
DROP TABLE IF EXISTS `t_job_address`;
CREATE TABLE `t_job_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL,
  `address` varchar(100) NOT NULL DEFAULT '',
  `latitude` double NOT NULL DEFAULT '0',
  `longitude` double NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `t_job_address_UN` (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;