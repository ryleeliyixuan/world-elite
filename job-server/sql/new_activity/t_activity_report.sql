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

 Date: 31/12/2020 20:24:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity_report
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_report`;
CREATE TABLE `t_activity_report`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '举报ID',
  `activity_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '活动ID',
  `report_user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '举报者用户ID',
  `option_id` int(11) NULL DEFAULT 0 COMMENT '举报理由选项',
  `reason` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '举报原因',
  `evidence` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '证据图片url,多个用;隔开',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '举报状态, 0:审核中 1:驳回举报,2:下架活动',
  `result` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '举报处理结果',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动举报表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
