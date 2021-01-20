create table t_resume_certificate
(
    id          int auto_increment
        primary key,
    resume_id   bigint                              null,
    title       varchar(255)                        null,
    time        timestamp                           null,
    create_time timestamp default CURRENT_TIMESTAMP null,
    update_time timestamp default CURRENT_TIMESTAMP null
)
    charset = utf8;