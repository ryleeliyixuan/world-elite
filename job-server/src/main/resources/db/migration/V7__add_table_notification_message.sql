SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_notification_message
-- ----------------------------
DROP TABLE IF EXISTS `t_notification_message`;
CREATE TABLE `t_notification_message`
(
    `id`          int(11)                                                        NOT NULL AUTO_INCREMENT COMMENT '消息id',
    `user_id`     bigint(20)                                                     NOT NULL COMMENT '消息归属用户id',
    `object_id`   varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '业务对象id',
    `type`        tinyint(1)                                                     NOT NULL COMMENT '消息业务类型',
    `msg_content` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '消息内容',
    `del_flag`    tinyint(4)                                                     NOT NULL DEFAULT 0,
    `create_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '消息通知表'
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
