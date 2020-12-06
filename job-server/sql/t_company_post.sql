
ALTER TABLE db_worldelite.t_company_post ADD recommend CHAR(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' NOT NULL COMMENT '是否推荐';
ALTER TABLE db_worldelite.t_company_post CHANGE recommend recommend CHAR(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' NOT NULL COMMENT '是否推荐' AFTER hots;
ALTER TABLE db_worldelite.t_company_post ADD image varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帖子图片';
ALTER TABLE db_worldelite.t_company_post CHANGE image image varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '帖子图片' AFTER tags;

ALTER TABLE db_worldelite.t_company_post MODIFY COLUMN recommend TINYINT(4) DEFAULT 0 NOT NULL COMMENT '是否推荐';
