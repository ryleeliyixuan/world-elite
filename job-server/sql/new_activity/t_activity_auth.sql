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
-- Table structure for t_activity_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_auth`;
CREATE TABLE `t_activity_auth`  (
  `id` int(11) NOT NULL COMMENT '活动id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '发布活动的用户id/企业id',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核失败原因',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  CONSTRAINT `fk_auth_activity_id` FOREIGN KEY (`id`) REFERENCES `t_activity` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
