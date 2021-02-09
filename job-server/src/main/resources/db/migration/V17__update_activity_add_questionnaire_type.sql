ALTER TABLE t_activity ADD questionnaire_type char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 0 NOT NULL COMMENT '报名表类型，0=报名表，1=模板';
ALTER TABLE t_activity CHANGE questionnaire_type questionnaire_type char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT 0 NOT NULL COMMENT '报名表类型，0=报名表，1=模板' AFTER questionnaire_id;
