DROP TABLE IF EXISTS `t_op_user`;
CREATE TABLE `t_op_user` (
	id INT(11) auto_increment NOT NULL COMMENT 'ID',
	user_id BIGINT(20) DEFAULT 0 NOT NULL COMMENT '用户ID',
	create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
	update_time timestamp DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL COMMENT '更新时间',
	CONSTRAINT t_op_user_PK PRIMARY KEY (id),
	CONSTRAINT t_op_user_UN UNIQUE KEY (user_id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
COMMENT='OP帐号表';
