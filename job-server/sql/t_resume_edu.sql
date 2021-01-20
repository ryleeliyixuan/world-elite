create table t_resume_edu
(
    id          int auto_increment
        primary key,
    resume_id   bigint       default 0                 not null,
    user_id     bigint       default 0                 not null,
    school_id   int          default 0                 not null comment '学校Id',
    school_name varchar(100) default ''                not null comment '学校名',
    major_id    int          default 0                 not null comment '专业ID',
    major_name  varchar(100) default ''                not null comment '专业名',
    degree_id   int          default 0                 not null comment '学历ID',
    start_time  timestamp                              null,
    finish_time timestamp                              null,
    gpa         double       default 0                 not null comment '绩点',
    position    int          default 0                 not null comment '排序位',
    create_time timestamp    default CURRENT_TIMESTAMP not null,
    update_time timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);