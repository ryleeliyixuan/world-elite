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

 Date: 20/08/2020 14:50:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_job_category_skill
-- ----------------------------
DROP TABLE IF EXISTS `t_job_category_skill`;
CREATE TABLE `t_job_category_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '职位类别-技能关联表id',
  `category_id` int(11) DEFAULT NULL COMMENT '职位类别id',
  `skill_id` int(11) DEFAULT NULL COMMENT '技能id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `category_id_fk`(`category_id`) USING BTREE,
  INDEX `skill_id_fk`(`skill_id`) USING BTREE,
  CONSTRAINT `category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `t_job_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `skill_id_fk` FOREIGN KEY (`skill_id`) REFERENCES `t_job_skill` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
