create table t_resume_experience
(
    id          int auto_increment
        primary key,
    resume_id   bigint       default 0                 not null,
    start_time  timestamp                              null,
    finish_time timestamp                              null,
    on_work     tinyint      default 0                 not null comment '在职',
    company     varchar(100) default ''                not null comment '公司',
    depart      varchar(50)  default ''                not null,
    post        varchar(50)  default ''                not null comment '职位',
    description text                                   null comment '描述',
    position    int          default 0                 not null comment '排序位',
    create_time timestamp    default CURRENT_TIMESTAMP not null,
    update_time timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    work_type   varchar(50)                            null,
    industry    varchar(50)                            null
)
    comment '工作经验';