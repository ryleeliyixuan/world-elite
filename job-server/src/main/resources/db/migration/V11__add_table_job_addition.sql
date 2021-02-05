-- 添加职位其他要求表
DROP TABLE IF EXISTS `t_job_addition`;
CREATE TABLE `t_job_addition` (
	id INT(11) auto_increment NOT NULL COMMENT 'ID',
	job_id BIGINT(20) DEFAULT 0 NOT NULL COMMENT '职位ID',
	addition_id INT(11) DEFAULT 0 NOT NULL COMMENT '其他要求字典ID',
	create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
	update_time timestamp DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
	CONSTRAINT t_job_addition_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='职位其他要求表';
