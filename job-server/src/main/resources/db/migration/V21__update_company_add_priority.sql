ALTER TABLE `db_worldelite`.`t_company`
    ADD COLUMN `priority` tinyint(4) NOT NULL DEFAULT 0 COMMENT '排序优先级, 越大越高' AFTER `industry`;