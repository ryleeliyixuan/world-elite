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

 Date: 24/12/2020 21:34:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_registration
-- ----------------------------
DROP TABLE IF EXISTS `t_registration`;
CREATE TABLE `t_registration`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '报名表id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '活动发布人id',
  `activity_id` int(11) NULL DEFAULT NULL COMMENT '活动id',
  `number` int(11) NULL DEFAULT NULL COMMENT '报名表编号',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报名者姓名',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `school` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学校',
  `grade` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级',
  `profession` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `education` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学历',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '审核状态',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_registration_activity_id`(`activity_id`) USING BTREE,
  CONSTRAINT `fk_registration_activity_id` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报名表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

-- 添加报名者ID和简历ID
ALTER TABLE t_registration ADD registration_user_id BIGINT(20) NULL COMMENT '活动报名者ID';
ALTER TABLE t_registration CHANGE registration_user_id registration_user_id BIGINT(20) NULL COMMENT '活动报名者ID' AFTER activity_id;
ALTER TABLE t_registration ADD resume_id BIGINT(20) NULL COMMENT '简历ID';
ALTER TABLE t_registration CHANGE resume_id resume_id BIGINT(20) NULL COMMENT '简历ID' AFTER registration_user_id;

