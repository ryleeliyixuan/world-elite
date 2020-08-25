/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.130.200
 Source Server Type    : MySQL
 Source Server Version : 50709
 Source Host           : 192.168.130.200:33306
 Source Schema         : db_worldelite

 Target Server Type    : MySQL
 Target Server Version : 50709
 File Encoding         : 65001

 Date: 20/08/2020 13:57:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_job_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_job_skill`;
CREATE TABLE `t_job_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '技能id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '技能名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
