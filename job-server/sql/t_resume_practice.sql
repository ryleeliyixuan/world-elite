create table t_resume_practice
(
    id          int auto_increment
        primary key,
    resume_id   bigint      default 0                 not null,
    start_time  timestamp                             null,
    finish_time timestamp                             null,
    title       varchar(50) default ''                not null,
    post        varchar(50) default ''                not null comment '职位',
    description text                                  null,
    position    int         default 0                 not null,
    create_time timestamp   default CURRENT_TIMESTAMP not null,
    update_time timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    on_work     tinyint     default 0                 null comment '实践中'
)
    comment '校园实践';