create table t_resume_skill
(
    id          int auto_increment
        primary key,
    resume_id   bigint      default 0                 not null,
    name        varchar(45) default ''                not null,
    level       tinyint     default 0                 not null,
    create_time timestamp   default CURRENT_TIMESTAMP not null,
    update_time timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint resume_name_index
        unique (resume_id, name)
)
    comment '技能';