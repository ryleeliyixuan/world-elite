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

 Date: 19/08/2020 23:26:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_job_category_industry
-- ----------------------------
DROP TABLE IF EXISTS `t_job_category_industry`;
CREATE TABLE `t_job_category_industry`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL DEFAULT 0 COMMENT '职位类型ID',
  `industry_id` bigint NOT NULL DEFAULT 0 COMMENT '行业领域ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `industry_id`(`industry_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '职位类型-行业对应表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_job_category_industry
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
