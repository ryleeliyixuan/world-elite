/*
 Navicat Premium Data Transfer

 Source Server         : www.kaers.top
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : www.kaers.top:33306
 Source Schema         : db_worldelite

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 19/01/2021 20:52:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_organizer_credit
-- ----------------------------
DROP TABLE IF EXISTS `t_organizer_credit`;
CREATE TABLE `t_organizer_credit`  (
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '主办方用户id',
  `credit` tinyint(1) NOT NULL DEFAULT 0 COMMENT '主办方信用等级',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '主办方信用等级白名单表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
