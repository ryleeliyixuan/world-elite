-- 添加template字段区分问题为报名表问题还是模板问题
ALTER TABLE t_questionnaire ADD template TINYINT(1) DEFAULT 0 NOT NULL COMMENT '是否为模板的问题';
ALTER TABLE t_questionnaire CHANGE template template TINYINT(1) DEFAULT 0 NOT NULL COMMENT '是否为模板的问题' AFTER `type`;
