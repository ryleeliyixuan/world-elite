SET MODE MYSQL;
SET REFERENTIAL_INTEGRITY FALSE;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `city_id` int(11) NOT NULL DEFAULT 0,
  `title` varchar(100)  NOT NULL DEFAULT '',
  `thumbnail` varchar(100)  NOT NULL DEFAULT '' COMMENT '图片',
  `summary` varchar(512)  NOT NULL DEFAULT '',
  `url` varchar(45)  NOT NULL DEFAULT '' COMMENT '链接',
  `description` longtext ,
  `status` tinyint(4) NOT NULL DEFAULT 0,
  `start_time` timestamp DEFAULT NULL,
  `finish_time` timestamp DEFAULT NULL,
  `address` varchar(100)  NOT NULL DEFAULT '',
  `organizer` varchar(100)  NOT NULL DEFAULT '' COMMENT '主办方',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 117 ;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50)  NOT NULL DEFAULT '',
  `email` varchar(60)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_code` varchar(10)  NOT NULL DEFAULT '',
  `phone` bigint(20) NOT NULL DEFAULT 0,
  `avatar` varchar(100)  NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT 0,
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0 正常，1 停用，3 离职',
  `password` varchar(256)  NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(50)  NOT NULL DEFAULT '' COMMENT '密码盐',
  `token` varchar(40)  NOT NULL DEFAULT '',
  `expired` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Token 过期时间，单位：秒',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `subscribe_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订阅信息',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE`(`email`)
) AUTO_INCREMENT = 1227385762795638786 ;

-- ----------------------------
-- Table structure for t_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '用户ID',
  `auth_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '平台类型',
  `auth_id` varchar(64) NOT NULL DEFAULT '' COMMENT '唯一ID',
  `open_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'openID',
  `verified` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否绑定',
  `expire_time` bigint(20) NOT NULL DEFAULT 0 COMMENT '过期时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 39 ;

-- ----------------------------
-- Table structure for t_company
-- ----------------------------
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company`  (
  `id` bigint(20) NOT NULL,
  `name` varchar(50)  NOT NULL DEFAULT '' COMMENT '简称',
  `full_name` varchar(150)  NOT NULL DEFAULT '' COMMENT '全称',
  `logo` varchar(100)  NOT NULL DEFAULT '',
  `scale_id` int(11) NOT NULL DEFAULT 0 COMMENT '公司规模',
  `stage_id` int(11) NOT NULL DEFAULT 0 COMMENT '公司阶段',
  `industry_id` int(11) NOT NULL DEFAULT 0 COMMENT '所属行业',
  `property_id` int(11) NOT NULL DEFAULT 0 COMMENT '企业性质',
  `stock_num` varchar(20)  NOT NULL DEFAULT '' COMMENT '股票代码',
  `homepage` varchar(100)  NOT NULL DEFAULT '' COMMENT '主页',
  `synopsis` varchar(150)  NOT NULL DEFAULT '' COMMENT '一句话介绍',
  `introduction` text  COMMENT '公司介绍',
  `regist_addr` varchar(150)  NOT NULL DEFAULT '' COMMENT '注册地址',
  `industry` varchar(45)  NOT NULL DEFAULT '' COMMENT '所属行业',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `full_name_UNIQUE`(`full_name`)
) ;

-- ----------------------------
-- Table structure for t_company_address
-- ----------------------------
DROP TABLE IF EXISTS `t_company_address`;
CREATE TABLE `t_company_address`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL,
  `address` varchar(100)  NOT NULL DEFAULT '',
  `city_id` int(11) NOT NULL DEFAULT 0,
  `latitude` double NOT NULL DEFAULT 0,
  `longitude` double NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 15 ;

-- ----------------------------
-- Table structure for t_company_pic
-- ----------------------------
DROP TABLE IF EXISTS `t_company_pic`;
CREATE TABLE `t_company_pic`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL DEFAULT 0,
  `url` varchar(100)  NOT NULL DEFAULT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1 ;

-- ----------------------------
-- Table structure for t_company_user
-- ----------------------------
DROP TABLE IF EXISTS `t_company_user`;
CREATE TABLE `t_company_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `company_id` bigint(20) NOT NULL DEFAULT 0,
  `depart` varchar(45)  NOT NULL DEFAULT '' COMMENT '部门',
  `post` varchar(45)  NOT NULL DEFAULT '' COMMENT '岗位',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_index`(`user_id`)
) AUTO_INCREMENT = 26 ;

-- ----------------------------
-- Table structure for t_company_verification
-- ----------------------------
DROP TABLE IF EXISTS `t_company_verification`;
CREATE TABLE `t_company_verification`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `id_card` varchar(45)  NOT NULL DEFAULT '' COMMENT '身份证',
  `id_card_pic` varchar(100)  NOT NULL DEFAULT '' COMMENT '身份证正面',
  `real_name` varchar(45)  NOT NULL DEFAULT '',
  `company` varchar(100)  NOT NULL DEFAULT '',
  `post` varchar(45)  NOT NULL DEFAULT '',
  `company_license_pic` varchar(100)  NOT NULL DEFAULT '' COMMENT '公司营业执照',
  `status` tinyint(4) NOT NULL DEFAULT 0,
  `remark` varchar(255)  NOT NULL DEFAULT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_id_UNIQUE`(`user_id`)
) AUTO_INCREMENT = 26 ;

-- ----------------------------
-- Table structure for t_company_wiki
-- ----------------------------
DROP TABLE IF EXISTS `t_company_wiki`;
CREATE TABLE `t_company_wiki`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL DEFAULT 0,
  `summary` varchar(500)  NOT NULL DEFAULT '' COMMENT '摘要',
  `content` longtext ,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 42 ;

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NOT NULL DEFAULT 0,
  `content` text  NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 11 ;

-- ----------------------------
-- Table structure for t_country
-- ----------------------------
DROP TABLE IF EXISTS `t_country`;
CREATE TABLE `t_country`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `english_name` varchar(45)  NOT NULL,
  `chinese_name` varchar(45)  NOT NULL,
  `country_code` varchar(10)  NOT NULL,
  `phone_code` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 217 ;

-- ----------------------------
-- Table structure for t_dict
-- ----------------------------
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(40) NOT NULL DEFAULT '',
  `type` int(11) NOT NULL COMMENT '1: 衣服款式\n2: 衣服颜色\n3: 衣服尺寸\n4: 店铺',
  `value` varchar(50) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_type_index`(`name`, `type`)
) AUTO_INCREMENT = 153 ;

-- ----------------------------
-- Table structure for t_download
-- ----------------------------
DROP TABLE IF EXISTS `t_download`;
CREATE TABLE `t_download`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `url` varchar(100) NOT NULL DEFAULT '',
  `progress` int(11) NOT NULL DEFAULT 0 COMMENT '处理进度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1 ;

-- ----------------------------
-- Table structure for t_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `object_id` bigint(20) NOT NULL DEFAULT 0,
  `type` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 82 ;

-- ----------------------------
-- Table structure for t_job
-- ----------------------------
DROP TABLE IF EXISTS `t_job`;
CREATE TABLE `t_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL DEFAULT 0,
  `creator_id` bigint(20) NOT NULL DEFAULT 0,
  `job_type` int(11) NOT NULL DEFAULT 0 COMMENT '招聘类型',
  `name` varchar(40)  NOT NULL DEFAULT '',
  `category_id` int(11) NOT NULL DEFAULT 0,
  `depart` varchar(45)  NOT NULL DEFAULT '' COMMENT '所属部门',
  `description` text  NOT NULL COMMENT '职位描述',
  `min_degree_id` int(11) NOT NULL DEFAULT 0 COMMENT '最低学历',
  `min_salary` int(11) NOT NULL DEFAULT 0 COMMENT '薪酬范围',
  `max_salary` int(11) NOT NULL DEFAULT 0,
  `salary_months` int(11) DEFAULT 0 COMMENT '薪资发放月数',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '工作城市',
  `address_id` int(11) NOT NULL DEFAULT 0 COMMENT '工作地点',
  `address` varchar(255)  NOT NULL DEFAULT '' COMMENT '工作地址',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '职位状态',
  `pub_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `remark` varchar(255)  NOT NULL DEFAULT '' COMMENT '备注',
  `experience` varchar(255)  DEFAULT NULL COMMENT '工作经验',
  `industry_tags` varchar(255)  DEFAULT NULL COMMENT '行业标签',
  `skill_tags` varchar(255)  DEFAULT NULL COMMENT '技能标签',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1300334443023978497 ;

-- ----------------------------
-- Table structure for t_job_apply
-- ----------------------------
DROP TABLE IF EXISTS `t_job_apply`;
CREATE TABLE `t_job_apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL DEFAULT 0,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `status` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 47 ;

-- ----------------------------
-- Table structure for t_job_category
-- ----------------------------
DROP TABLE IF EXISTS `t_job_category`;
CREATE TABLE `t_job_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(45)  NOT NULL DEFAULT '',
  `position` int(11) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `parent_id_name`(`parent_id`, `name`)
) AUTO_INCREMENT = 1004 ;

-- ----------------------------
-- Table structure for t_job_category_industry
-- ----------------------------
DROP TABLE IF EXISTS `t_job_category_industry`;
CREATE TABLE `t_job_category_industry`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '职位类型ID',
  `industry_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '行业领域ID',
  PRIMARY KEY (`id`) ,
  INDEX `industry_id`(`industry_id`)
) AUTO_INCREMENT = 1 ;


-- ----------------------------
-- Table structure for t_job_desc_example
-- ----------------------------
DROP TABLE IF EXISTS `t_job_desc_example`;
CREATE TABLE `t_job_desc_example`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '职位类型ID',
  `description` varchar(2000)  NOT NULL DEFAULT '' COMMENT '职位类型描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` varchar(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1 ;

-- ----------------------------
-- Table structure for t_job_industry
-- ----------------------------
DROP TABLE IF EXISTS `t_job_industry`;
CREATE TABLE `t_job_industry`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100)  NOT NULL COMMENT '行业名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` varchar(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1 ;

-- ----------------------------
-- Table structure for t_job_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_job_skill`;
CREATE TABLE `t_job_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '技能id',
  `name` varchar(255)  DEFAULT NULL COMMENT '技能名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 3 ;

-- ----------------------------
-- Table structure for t_job_category_skill
-- H2不能很好的兼容 外键sql被移除
-- ----------------------------
DROP TABLE IF EXISTS `t_job_category_skill`;
CREATE TABLE `t_job_category_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '职位类别-技能关联表id',
  `category_id` int(11) DEFAULT NULL COMMENT '职位类别id',
  `skill_id` int(11) DEFAULT NULL COMMENT '技能id',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 6 ;

-- ----------------------------
-- Table structure for t_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_login_log`;
CREATE TABLE `t_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `ip` varchar(60) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1310 ;

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_user` bigint(20) NOT NULL DEFAULT 0,
  `to_user` bigint(20) NOT NULL DEFAULT 0,
  `content` varchar(255) NOT NULL DEFAULT '',
  `url` varchar(100) NOT NULL DEFAULT '',
  `read_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 159 ;

-- ----------------------------
-- Table structure for t_recommend
-- ----------------------------
DROP TABLE IF EXISTS `t_recommend`;
CREATE TABLE `t_recommend`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_id` bigint(20) NOT NULL DEFAULT 0,
  `object_type` tinyint(4) NOT NULL DEFAULT 0,
  `position` int(11) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 77 ;

-- ----------------------------
-- Table structure for t_resume
-- ----------------------------
DROP TABLE IF EXISTS `t_resume`;
CREATE TABLE `t_resume`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `name` varchar(100)  NOT NULL DEFAULT '' COMMENT '姓名',
  `birth` timestamp DEFAULT NULL,
  `gender` tinyint(4) NOT NULL DEFAULT 0,
  `country_id` int(11) NOT NULL DEFAULT 0 COMMENT '国籍',
  `max_degree_id` int(11) NOT NULL DEFAULT 0 COMMENT '最高学历',
  `marital_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '婚姻状况',
  `return_time` timestamp DEFAULT NULL COMMENT '回国时间',
  `graduate_time` timestamp DEFAULT NULL COMMENT '毕业时间',
  `cur_place` varchar(100)  NOT NULL DEFAULT '' COMMENT '现居地址',
  `lang` tinyint(4) NOT NULL DEFAULT 0 COMMENT '语言',
  `attach_resume` varchar(100)  NOT NULL DEFAULT '' COMMENT '附件简历',
  `introduction` text  COMMENT '自我介绍',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Table structure for t_resume_attach
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_attach`;
CREATE TABLE `t_resume_attach`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `resume_id` bigint(20) NOT NULL COMMENT '简历id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `attach_content` mediumtext  NOT NULL COMMENT '附件简历内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 1 ;

-- ----------------------------
-- Table structure for t_resume_edu
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_edu`;
CREATE TABLE `t_resume_edu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `school_id` int(11) NOT NULL DEFAULT 0 COMMENT '学校Id',
  `school_name` varchar(100)  NOT NULL DEFAULT '' COMMENT '学校名',
  `major_id` int(11) NOT NULL DEFAULT 0 COMMENT '专业ID',
  `major_name` varchar(100)  NOT NULL DEFAULT '' COMMENT '专业名',
  `degree_id` int(11) NOT NULL DEFAULT 0 COMMENT '学历ID',
  `start_time` timestamp DEFAULT NULL,
  `finish_time` timestamp DEFAULT NULL,
  `gpa` double NOT NULL DEFAULT 0 COMMENT '绩点',
  `position` int(11) NOT NULL DEFAULT 0 COMMENT '排序位',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 69 ;

-- ----------------------------
-- Table structure for t_resume_experience
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_experience`;
CREATE TABLE `t_resume_experience`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `start_time` timestamp DEFAULT NULL,
  `finish_time` timestamp DEFAULT NULL,
  `on_work` tinyint(4) NOT NULL DEFAULT 0 COMMENT '在职',
  `company` varchar(100)  NOT NULL DEFAULT '' COMMENT '公司',
  `depart` varchar(50)  NOT NULL DEFAULT '',
  `post` varchar(50)  NOT NULL DEFAULT '' COMMENT '职位',
  `description` text  COMMENT '描述',
  `position` int(11) NOT NULL DEFAULT 0 COMMENT '排序位',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 71 ;

-- ----------------------------
-- Table structure for t_resume_link
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_link`;
CREATE TABLE `t_resume_link`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `name` varchar(45)  NOT NULL DEFAULT '',
  `icon` varchar(45)  NOT NULL DEFAULT '',
  `link` varchar(100)  NOT NULL DEFAULT '',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 34 ;

-- ----------------------------
-- Table structure for t_resume_practice
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_practice`;
CREATE TABLE `t_resume_practice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `start_time` timestamp DEFAULT NULL,
  `finish_time` timestamp DEFAULT NULL,
  `title` varchar(50)  NOT NULL DEFAULT '',
  `post` varchar(50)  NOT NULL DEFAULT '' COMMENT '职位',
  `description` text ,
  `position` int(11) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `on_work` tinyint(4) DEFAULT 0 COMMENT '实践中',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 67 ;

-- ----------------------------
-- Table structure for t_resume_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_resume_skill`;
CREATE TABLE `t_resume_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume_id` bigint(20) NOT NULL DEFAULT 0,
  `name` varchar(45)  NOT NULL DEFAULT '',
  `level` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `resume_name_index`(`resume_id`, `name`)
) AUTO_INCREMENT = 325 ;

-- ----------------------------
-- Table structure for t_school
-- ----------------------------
DROP TABLE IF EXISTS `t_school`;
CREATE TABLE `t_school`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `country_id` int(11) NOT NULL DEFAULT 0,
  `name` varchar(100)  NOT NULL DEFAULT '',
  `site_link` varchar(100)  NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE`(`name`)
) AUTO_INCREMENT = 19927 ;

-- ----------------------------
-- Table structure for t_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_log`;
CREATE TABLE `t_sys_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `user_ip` varchar(60) NOT NULL,
  `req_method` varchar(100) NOT NULL,
  `req_desc` text,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 446 ;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50)  NOT NULL DEFAULT '',
  `email` varchar(60)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_code` varchar(10)  NOT NULL DEFAULT '',
  `phone` bigint(20) NOT NULL DEFAULT 0,
  `avatar` varchar(100)  NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT 0,
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0 正常，1 停用，3 离职',
  `password` varchar(256)  NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(50)  NOT NULL DEFAULT '' COMMENT '密码盐',
  `token` varchar(40)  NOT NULL DEFAULT '',
  `expired` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Token 过期时间，单位：秒',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `subscribe_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订阅信息',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `t_user_email_UNIQUE`(`email`)
) AUTO_INCREMENT = 1289049897187487745 ;

-- ----------------------------
-- Table structure for t_user_applicant
-- ----------------------------
DROP TABLE IF EXISTS `t_user_applicant`;
CREATE TABLE `t_user_applicant`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50)  NOT NULL DEFAULT '',
  `email` varchar(60)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_code` varchar(10)  NOT NULL DEFAULT '',
  `phone` bigint(20) NOT NULL DEFAULT 0,
  `avatar` varchar(100)  NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT 0,
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0 正常，1 停用，3 离职',
  `password` varchar(256)  NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(50)  NOT NULL DEFAULT '' COMMENT '密码盐',
  `token` varchar(40)  NOT NULL DEFAULT '',
  `expired` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Token 过期时间，单位：秒',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `subscribe_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订阅信息',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `t_user_applicant_email_UNIQUE`(`email`)
) AUTO_INCREMENT = 1294004007699546113 ;

-- ----------------------------
-- Table structure for t_user_corporate
-- ----------------------------
DROP TABLE IF EXISTS `t_user_corporate`;
CREATE TABLE `t_user_corporate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50)  NOT NULL DEFAULT '',
  `email` varchar(60)  NOT NULL DEFAULT '' COMMENT '用户邮箱',
  `phone_code` varchar(10)  NOT NULL DEFAULT '',
  `phone` bigint(20) NOT NULL DEFAULT 0,
  `avatar` varchar(100)  NOT NULL DEFAULT '',
  `gender` tinyint(4) NOT NULL DEFAULT 0,
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '用户类型',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '状态：0 正常，1 停用，3 离职',
  `password` varchar(256)  NOT NULL DEFAULT '' COMMENT '密码',
  `salt` varchar(50)  NOT NULL DEFAULT '' COMMENT '密码盐',
  `token` varchar(40)  NOT NULL DEFAULT '',
  `expired` bigint(20) NOT NULL DEFAULT 0 COMMENT 'Token 过期时间，单位：秒',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `subscribe_flag` tinyint(4) NOT NULL DEFAULT 0 COMMENT '订阅信息',
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `t_user_corporate_email_UNIQUE`(`email`)
) AUTO_INCREMENT = 1292660938492928001 ;

-- ----------------------------
-- Table structure for t_user_corporate_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_user_corporate_comment`;
CREATE TABLE `t_user_corporate_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_apply_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '职位申请ID',
  `comment` varchar(100)  NOT NULL DEFAULT '' COMMENT '笔记',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 24 ;

-- ----------------------------
-- Table structure for t_user_corporate_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_user_corporate_tag`;
CREATE TABLE `t_user_corporate_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_apply_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '职位申请ID',
  `tag_name` varchar(255)  NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  `del_flag` varchar(1)  NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) AUTO_INCREMENT = 13 ;

-- ----------------------------
-- Table structure for t_user_expect_job
-- ----------------------------
DROP TABLE IF EXISTS `t_user_expect_job`;
CREATE TABLE `t_user_expect_job`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `category_id` int(11) NOT NULL DEFAULT 0 COMMENT '岗位ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_category_id`(`user_id`, `category_id`)
) AUTO_INCREMENT = 68 ;

-- ----------------------------
-- Table structure for t_user_expect_place
-- ----------------------------
DROP TABLE IF EXISTS `t_user_expect_place`;
CREATE TABLE `t_user_expect_place`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `city_id` int(11) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_city_index`(`user_id`, `city_id`)
) AUTO_INCREMENT = 68 ;

-- ----------------------------
-- Table structure for t_user_expect_salary
-- ----------------------------
DROP TABLE IF EXISTS `t_user_expect_salary`;
CREATE TABLE `t_user_expect_salary`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL DEFAULT 0,
  `min_value` int(11) NOT NULL DEFAULT 0 COMMENT '单位：千',
  `max_value` int(11) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_timestamp,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `user_id_index`(`user_id`)
) AUTO_INCREMENT = 15 ;

SET FOREIGN_KEY_CHECKS = 1;
