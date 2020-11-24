CREATE TABLE `t_company_recommend` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`company_id` BIGINT(20) NOT NULL DEFAULT '0',
	`object_id` BIGINT(20) NOT NULL DEFAULT '0',
	`object_type` TINYINT(4) NOT NULL DEFAULT '0',
	`position` INT(11) NOT NULL DEFAULT '0',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='公司模块推荐表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;