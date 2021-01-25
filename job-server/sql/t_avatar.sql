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

 Date: 21/01/2021 19:59:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_avatar
-- ----------------------------
DROP TABLE IF EXISTS `t_avatar`;
CREATE TABLE `t_avatar`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像url链接',
  `type` tinyint(1) NULL DEFAULT 0 COMMENT '头像类型',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标记, 0:未删除;1:已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '头像' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
