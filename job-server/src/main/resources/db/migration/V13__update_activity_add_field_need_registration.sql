ALTER TABLE `db_worldelite`.`t_activity`
    ADD COLUMN `need_registration` tinyint(1) NULL DEFAULT 1 COMMENT '是否需要报名, 0不需要,1需要' AFTER `registration_finish_time`;