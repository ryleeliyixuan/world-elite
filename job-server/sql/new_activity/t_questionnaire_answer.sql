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

 Date: 24/12/2020 21:33:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_questionnaire_answer
-- ----------------------------
DROP TABLE IF EXISTS `t_questionnaire_answer`;
CREATE TABLE `t_questionnaire_answer`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '问卷回答id',
  `registration_id` int(11) NULL DEFAULT NULL COMMENT '报名表id',
  `questionnaire_id` int(11) NULL DEFAULT NULL COMMENT '问卷表id',
  `answer_content` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回答内容',
  `answer_options_id` int(11) NULL DEFAULT NULL COMMENT '回答选项id(如果是选择题)',
  `del_flag` tinyint(4) NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_answer_registration_id`(`registration_id`) USING BTREE,
  INDEX `fk_answer_questionnaire_id`(`questionnaire_id`) USING BTREE,
  CONSTRAINT `fk_answer_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES `t_questionnaire` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_answer_registration_id` FOREIGN KEY (`registration_id`) REFERENCES `t_registration` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '报名问卷回答表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
