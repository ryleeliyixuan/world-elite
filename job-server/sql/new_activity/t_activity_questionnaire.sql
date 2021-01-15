-- db_worldelite_activity.t_questionnaire_template definition

CREATE TABLE `t_activity_questionnaire` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报名表id',
  `activity_id` int(11) DEFAULT NULL COMMENT '活动id',
  `title` varchar(100) NOT NULL COMMENT '报名表标题',
  `name_flag` char(1) DEFAULT NULL COMMENT '姓名选填flag.0 显示,1 选填,2 隐藏',
  `gender_flag` char(1) DEFAULT '0' COMMENT '性别选填/隐藏flag. 0显示,1选填,2隐藏',
  `phone_flag` char(1) DEFAULT '0' COMMENT '手机号选填/隐藏flag. 0显示,1选填,2隐藏',
  `email_flag` char(1) DEFAULT '0' COMMENT '邮箱选填/隐藏flag. 0显示,1选填,2隐藏',
  `school_flag` char(1) DEFAULT '0' COMMENT '学校选填/隐藏flag. 0显示,1选填,2隐藏',
  `grade_flag` char(1) DEFAULT '0' COMMENT '年级选填/隐藏flag. 0显示,1选填,2隐藏',
  `profession_flag` char(1) DEFAULT '0' COMMENT '专业选填/隐藏flag. 0显示,1选填,2隐藏',
  `education_flag` char(1) DEFAULT '0' COMMENT '学历选填/隐藏flag. 0显示,1选填,2隐藏',
  `del_flag` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='活动报名表';

ALTER TABLE db_worldelite_activity.t_activity_questionnaire CHANGE activity_id template_id int(11) NULL COMMENT '模板id';
