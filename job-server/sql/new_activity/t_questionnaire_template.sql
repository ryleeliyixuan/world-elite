/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.130.200
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : 192.168.130.200:33306
 Source Schema         : db_worldelite_activity

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 24/12/2020 21:40:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_questionnaire_template
-- ----------------------------
DROP TABLE IF EXISTS `t_questionnaire_template`;
CREATE TABLE `t_questionnaire_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报名模板表id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '活动发布人id',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动id',
  `gender_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '性别选填/隐藏flag. 0显示,1选填,2隐藏',
  `phone_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '手机号选填/隐藏flag. 0显示,1选填,2隐藏',
  `email_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '邮箱选填/隐藏flag. 0显示,1选填,2隐藏',
  `school_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '学校选填/隐藏flag. 0显示,1选填,2隐藏',
  `grade_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '年级选填/隐藏flag. 0显示,1选填,2隐藏',
  `profession_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '专业选填/隐藏flag. 0显示,1选填,2隐藏',
  `education_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '学历选填/隐藏flag. 0显示,1选填,2隐藏',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_template_activity_id`(`activity_id`) USING BTREE,
  CONSTRAINT `fk_template_activity_id` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报名问模板表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 添加模板名，模板标题和用户姓名
ALTER TABLE t_questionnaire_template ADD template_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' NOT NULL COMMENT '模板名';
ALTER TABLE t_questionnaire_template CHANGE template_name template_name varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' NOT NULL COMMENT '模板名' AFTER activity_id;
ALTER TABLE t_questionnaire_template ADD title varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板标题';
ALTER TABLE t_questionnaire_template CHANGE title title varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板标题' AFTER template_name;
ALTER TABLE t_questionnaire_template ADD name_flag char(1) NULL COMMENT '姓名选填flag.0 显示,1 选填,2 隐藏';
ALTER TABLE t_questionnaire_template CHANGE name_flag name_flag char(1) NULL COMMENT '姓名选填flag.0 显示,1 选填,2 隐藏' AFTER title;

