/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : other

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-04-06 15:34:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for weibo
-- ----------------------------
DROP TABLE IF EXISTS `weibo`;
CREATE TABLE `weibo` (
  `w_url` varchar(255) NOT NULL,
  `w_ouid` varchar(255) DEFAULT NULL,
  `w_mid` varchar(255) DEFAULT NULL,
  `w_user_name` varchar(255) DEFAULT NULL,
  `w_user_url` varchar(255) DEFAULT NULL,
  `w_date` varchar(20) DEFAULT NULL,
  `w_from` varchar(255) DEFAULT NULL,
  `w_collect_count` varchar(255) DEFAULT NULL,
  `w_share_count` varchar(255) DEFAULT NULL,
  `w_comment_count` varchar(255) DEFAULT NULL,
  `w_thumb_count` varchar(255) DEFAULT NULL,
  `w_content` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `w_pics` varchar(2048) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `qid` varchar(256) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `full_content_param` longtext,
  PRIMARY KEY (`w_url`),
  KEY `key_qid` (`qid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
