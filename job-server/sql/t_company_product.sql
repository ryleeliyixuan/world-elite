CREATE TABLE `t_company_product` (
	`id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '产品ID',
	`company_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '企业ID',
	`name` VARCHAR(20) NULL DEFAULT '' COMMENT '产品名' COLLATE 'utf8mb4_general_ci',
	`description` VARCHAR(400) NULL DEFAULT '' COMMENT '产品描述' COLLATE 'utf8mb4_general_ci',
	`imageUrl` VARCHAR(255) NULL DEFAULT '' COMMENT '图片地址' COLLATE 'utf8mb4_general_ci',
	`imageName` VARCHAR(255) NULL DEFAULT '' COMMENT '图片名',
	`url` VARCHAR(255) NULL DEFAULT '' COMMENT '产品链接',
	`position` INT(11) NULL DEFAULT '0' COMMENT '排序',
	`create_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='企业产品表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

ALTER TABLE `t_company_product`
	CHANGE COLUMN `imageUrl` `image_url` VARCHAR(255) NULL DEFAULT '' COMMENT '图片地址' COLLATE 'utf8mb4_general_ci' AFTER `description`,
	CHANGE COLUMN `imageName` `image_name` VARCHAR(255) NULL DEFAULT '' COMMENT '图片名' COLLATE 'utf8mb4_general_ci' AFTER `image_url`;
