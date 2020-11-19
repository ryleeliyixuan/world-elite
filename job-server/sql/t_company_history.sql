ALTER TABLE `t_company_history`
	CHANGE COLUMN `event_time` `event_time` VARCHAR(20) NULL DEFAULT NULL COMMENT '事件时间' AFTER `company_id`;