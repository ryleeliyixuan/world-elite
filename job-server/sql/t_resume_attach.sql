create table t_resume_attach
(
    id             int auto_increment comment '自增id'
        primary key,
    resume_id      bigint                              not null comment '简历id',
    user_id        bigint                              not null comment '用户id',
    attach_content mediumtext charset utf8mb4          not null comment '附件简历内容',
    create_time    timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    charset = utf8;