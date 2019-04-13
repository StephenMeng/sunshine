/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : other

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-04-06 15:34:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for weibo_user_config
-- ----------------------------
DROP TABLE IF EXISTS `weibo_user_config`;
CREATE TABLE `weibo_user_config` (
  `uri` varchar(255) DEFAULT NULL,
  `oid` varchar(255) NOT NULL,
  `page_id` varchar(155) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `domain` varchar(20) DEFAULT NULL,
  `pids` varchar(255) DEFAULT NULL,
  `fans_num` int(30) DEFAULT NULL,
  `follow_num` int(30) DEFAULT NULL,
  `weibo_num` int(20) DEFAULT NULL,
  `lv` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `tag` varchar(512) DEFAULT NULL,
  `baike` varchar(255) DEFAULT NULL,
  `birthday` varchar(255) DEFAULT NULL,
  `career` varchar(255) DEFAULT NULL,
  `pinfo` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `update_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
