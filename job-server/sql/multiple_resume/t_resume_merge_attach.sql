create table t_resume_merge_attach
(
    id            int auto_increment
        primary key,
    resume_id     bigint                              null,
    resume_attach varchar(255)                        null,
    create_time   timestamp default CURRENT_TIMESTAMP null,
    update_time   timestamp default CURRENT_TIMESTAMP null,
    name          varchar(50)                         null
)
    charset = utf8;