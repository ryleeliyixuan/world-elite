DROP TABLE IF EXISTS `t_user_corporate_comment`;

CREATE TABLE `t_user_corporate_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_apply_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '职位申请ID',
  `comment` varchar(100) NOT NULL DEFAULT '' COMMENT '笔记',
  `del_flag` tinyint NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户笔记';

alter table `t_user_corporate_comment` change `job_apply_id` `resume_id` bigint(20) not null default '0' comment '简历ID';
alter table `t_user_corporate_comment` add `creator_id` bigint(20) not null default '0' comment '创建者ID';
