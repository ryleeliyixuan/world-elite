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

 Date: 14/08/2020 21:11:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_corporate_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_user_corporate_tag`;
CREATE TABLE `t_user_corporate_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `job_apply_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '职位申请ID',
  `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user_corporate_tag
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

alter table `t_user_corporate_tag` change `job_apply_id` `resume_id` bigint(20) not null default '0' comment '简历ID';
alter table `t_user_corporate_tag` add `creator_id` bigint(20) not null default '0' comment '创建者ID';
