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

 Date: 25/12/2020 14:42:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity_review
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_review`;
CREATE TABLE `t_activity_review`  (
  `activity_id` int(11) NOT NULL COMMENT '活动id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '发布活动的用户id/企业id',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '状态.1:审核中,2:通过,3拒绝',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核失败原因',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`activity_id`) USING BTREE,
  CONSTRAINT `fk_auth_activity_id` FOREIGN KEY (`activity_id`) REFERENCES `t_activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动审核表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
