create table t_user_expect_job
(
    id               int auto_increment
        primary key,
    resume_id        bigint                              null,
    expect_position  varchar(255)                        null,
    expect_work_type varchar(50)                         null,
    expect_city      varchar(50)                         null,
    category_id      int       default 0                 not null comment '岗位ID',
    salary_id        tinyint                             null,
    create_time      timestamp default CURRENT_TIMESTAMP not null,
    update_time      timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    industry         varchar(255)                        null
)
    comment '求职意向职位';