ALTER TABLE `t_company_wiki`
	CHANGE COLUMN `content` `content` LONGTEXT NULL DEFAULT NULL COMMENT '内容' COLLATE 'utf8mb4_general_ci' AFTER `summary`,
	ADD COLUMN `banner` VARCHAR(255) NULL DEFAULT NULL COMMENT '大图' AFTER `content`,
	CHANGE COLUMN `video` `video` VARCHAR(255) NULL DEFAULT NULL COMMENT '视频' COLLATE 'utf8mb4_general_ci' AFTER `banner`,
	CHANGE COLUMN `city_id` `address` VARCHAR(50) NULL DEFAULT NULL COMMENT '地址' AFTER `video`,
	DROP COLUMN `city_enable`,
	DROP COLUMN `employee_enable`,
	DROP COLUMN `product_enable`,
	DROP COLUMN `market_enable`,
	DROP COLUMN `history_enable`,
	DROP COLUMN `structure_enable`;

ALTER TABLE `db_worldelite`.`t_company_wiki`
MODIFY COLUMN `summary` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '摘要' AFTER `company_id`;