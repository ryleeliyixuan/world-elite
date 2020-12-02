-- 公司禁言表
CREATE TABLE `t_company_forbidden` (
	`id` INT(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`user_id` BIGINT(20) UNSIGNED ZEROFILL NOT NULL COMMENT '用户ID',
	`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='公司禁言表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

ALTER TABLE db_worldelite.t_company_forbidden ADD days INT DEFAULT 0 NOT NULL COMMENT '禁言天数';
ALTER TABLE db_worldelite.t_company_forbidden ADD content varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' NOT NULL;
ALTER TABLE db_worldelite.t_company_forbidden CHANGE days days_id int(11) DEFAULT 0 NOT NULL COMMENT '禁言天数';
