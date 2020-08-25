/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : db_worldelite

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 19/08/2020 23:26:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_job_industry
-- ----------------------------
DROP TABLE IF EXISTS `t_job_industry`;
CREATE TABLE `t_job_industry`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '行业名称',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '行业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_job_industry
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
