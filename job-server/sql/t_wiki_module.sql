CREATE TABLE `t_wiki_module` (
	`id` INT(11) NOT NULL AUTO_INCREMENT,
	`company_id` BIGINT(20) NOT NULL DEFAULT '0',
	`wiki_enable` TINYINT(4) NULL DEFAULT '1',
	`address_enable` TINYINT(4) NULL DEFAULT '1',
	`employee_enable` TINYINT(4) NOT NULL DEFAULT '1',
	`product_enable` TINYINT(4) NULL DEFAULT '1',
	`market_enable` TINYINT(4) NULL DEFAULT '1',
	`history_enable` TINYINT(4) NULL DEFAULT '1',
	`structure_enable` TINYINT(4) NULL DEFAULT '1',
	`salary_enable` TINYINT(4) NULL DEFAULT '1',
	`recruit_enable` TINYINT(4) NULL DEFAULT '1',
	`environment_enable` TINYINT(4) NULL DEFAULT '1',
	`department_enable` TINYINT(4) NULL DEFAULT '1',
	`honor_enable` TINYINT(4) NULL DEFAULT '1',
	`post_enable` TINYINT(4) NULL DEFAULT '1',
	`job_enable` TINYINT(4) NULL DEFAULT '1',
	`count_enable` TINYINT(4) NULL DEFAULT '1',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='百科模块表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
