-- 在已有数据表上改动的部分
ALTER TABLE t_resume ADD status TINYINT(4) DEFAULT 0 not null comment '简历状态';
ALTER TABLE t_resume ADD type TINYINT(4) DEFAULT 0 not null comment '简历类型';
ALTER TABLE t_resume ADD priority TINYINT(4) DEFAULT 0 NULL;
ALTER TABLE t_resume ADD email varchar(255) NULL;
ALTER TABLE t_resume ADD phone varchar(255) NULL;
ALTER TABLE t_resume ADD avatar varchar(255) NULL;
ALTER TABLE t_resume ADD title varchar(255) NULL;

ALTER TABLE t_resume_experience ADD work_type varchar(50) NULL;
ALTER TABLE t_resume_experience ADD industry varchar(50) NULL;

ALTER TABLE t_resume_practice ADD on_work tinyint default 0 null comment '实践中';

ALTER TABLE t_user_expect_job ADD industry varchar(255) null;
