create table t_resume
(
    id             bigint auto_increment
        primary key,
    user_id        bigint       default 0                 not null,
    name           varchar(100) default ''                not null comment '姓名',
    birth          timestamp                              null,
    gender         tinyint      default 0                 not null,
    country_id     int          default 0                 not null comment '国籍',
    max_degree_id  int          default 0                 not null comment '最高学历',
    marital_status tinyint      default 0                 not null comment '婚姻状况',
    return_time    timestamp                              null comment '回国时间',
    graduate_time  timestamp                              null comment '毕业时间',
    cur_place      varchar(100) default ''                not null comment '现居地址',
    lang           tinyint      default 0                 not null comment '语言',
    attach_resume  varchar(100) default ''                not null comment '附件简历',
    introduction   text                                   null comment '自我介绍',
    create_time    timestamp    default CURRENT_TIMESTAMP not null,
    update_time    timestamp    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    status         tinyint      default 0                 not null comment '简历状态',
    type           tinyint      default 0                 not null comment '简历类型',
    priority       tinyint      default 0                 null,
    email          varchar(255)                           null,
    phone          varchar(255)                           null,
    avatar         varchar(255)                           null,
    title          varchar(255)                           null
);