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

 Date: 24/12/2020 20:15:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `user_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '发布者id',
  `city_id` int(11) NOT NULL DEFAULT 0 COMMENT '城市id',
  `user_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发布账户类型(个人账户:1/企业账户:2/管理账户:100)',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '活动标题',
  `poster` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '活动海报',
  `organizer_id` int(11) NULL DEFAULT NULL COMMENT '主办方信息id',
  `organizer_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主办方类型',
  `activity_start_time` timestamp NULL DEFAULT NULL COMMENT '活动开始时间',
  `activity_finish_time` timestamp NULL DEFAULT NULL COMMENT '活动结束时间',
  `registration_start_time` timestamp NULL DEFAULT NULL COMMENT '报名开始时间',
  `registration_finish_time` timestamp NULL DEFAULT NULL COMMENT '报名结束时间',
  `need_resume` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否需要报名者简历信息, 0不需要,1需要',
  `only_overseas_student` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否仅留学生能参加,0不限制,1仅海外',
  `audit_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '报名审核类型(是否需要审核),0不需要,1需要',
  `number_limit` int(11) NULL DEFAULT NULL COMMENT '报名人数限制',
  `address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '详细地址, 如果是线上活动则为链接',
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '活动介绍',
  `status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '活动状态, 0:审核中 1:草稿,2:下架,3:即将开始,4:报名中,5:进行中,6已结束',
  `registration_template_id` int(11) NULL DEFAULT NULL COMMENT '报名表模板ID',
  `follower` int(11) NULL DEFAULT 0 COMMENT '关注人数',
  `applicant_quantity` int(11) NULL DEFAULT 0 COMMENT '报名人数',
  `weight` int(11) NULL DEFAULT 0 COMMENT '置顶权重',
  `notice` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '通知提示,0不再提示,1需要提示',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_organizer_id`(`organizer_id`) USING BTREE,
  CONSTRAINT `fk_organizer_id` FOREIGN KEY (`organizer_id`) REFERENCES `t_organizer_info` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
