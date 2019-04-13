/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : sunshine

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-04-14 00:46:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sunshine_article
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_article`;
CREATE TABLE `sunshine_article` (
  `article_id` int(30) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `article_link_id` varchar(64) NOT NULL,
  `channel_id` int(10) NOT NULL,
  `column_id` int(10) NOT NULL,
  `article_title` varchar(255) NOT NULL COMMENT '博客题名',
  `article_abstract` text NOT NULL COMMENT '博客摘要',
  `article_tag` text NOT NULL COMMENT '标签',
  `article_author` varchar(255) NOT NULL COMMENT '作者id',
  `article_comment_count` int(11) NOT NULL COMMENT '评论数',
  `article_view_count` int(11) NOT NULL COMMENT '浏览数',
  `article_content` mediumtext NOT NULL COMMENT '内容',
  `article_link` varchar(255) NOT NULL COMMENT '链接',
  `article_had_been_published` char(1) NOT NULL COMMENT '是否已被发布',
  `article_is_published` char(1) NOT NULL COMMENT '是否发布',
  `article_put_top` char(1) NOT NULL COMMENT '是否置顶',
  `article_create_date` datetime NOT NULL COMMENT '创建时间',
  `article_update_date` datetime NOT NULL COMMENT '更新时间',
  `article_view_pwd` varchar(100) DEFAULT NULL COMMENT '查看密码',
  `private` bit(1) NOT NULL COMMENT '是不是私有的，私有的是指对内展示的博客',
  `deleted` bit(1) NOT NULL,
  PRIMARY KEY (`article_id`),
  KEY `index_link_id` (`article_link_id`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COMMENT='utf8mb4编码';

-- ----------------------------
-- Table structure for sunshine_article_attach
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_article_attach`;
CREATE TABLE `sunshine_article_attach` (
  `article_id` int(30) NOT NULL,
  `attach_uri` varchar(255) NOT NULL,
  `attach_name` varchar(255) NOT NULL,
  PRIMARY KEY (`article_id`,`attach_uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sunshine_attachment
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_attachment`;
CREATE TABLE `sunshine_attachment` (
  `att_id` int(20) NOT NULL AUTO_INCREMENT,
  `att_type` varchar(255) NOT NULL COMMENT '文件类型',
  `uploader` int(30) NOT NULL COMMENT '上传者',
  `owner_id` int(30) NOT NULL COMMENT '所有者',
  `owner_type` int(10) NOT NULL COMMENT '所有者类型：1:文章，2：用户',
  `att_name` varchar(255) NOT NULL COMMENT '文件名',
  `att_uri` varchar(255) NOT NULL COMMENT '文件位置',
  `att_length` double NOT NULL COMMENT '文件长度',
  `upload_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_date` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`att_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_book
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_book`;
CREATE TABLE `sunshine_book` (
  `alt` varchar(255) NOT NULL,
  `title` varchar(512) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `binding` varchar(255) DEFAULT NULL,
  `catalog` longtext,
  `id` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `isbn10` varchar(255) DEFAULT NULL,
  `isbn13` varchar(255) DEFAULT NULL,
  `pages` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `pubdate` varchar(255) DEFAULT NULL,
  `publisher` varchar(255) DEFAULT NULL,
  `subtitle` varchar(255) DEFAULT NULL,
  `summary` longtext,
  `tags` varchar(255) DEFAULT NULL,
  `translator` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`alt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_channel
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_channel`;
CREATE TABLE `sunshine_channel` (
  `channel_id` int(10) NOT NULL AUTO_INCREMENT,
  `channel_uri` varchar(255) NOT NULL,
  `channel_name_cn` varchar(255) NOT NULL,
  `channel_name_en` varchar(255) NOT NULL,
  `has_column` bit(1) NOT NULL DEFAULT b'1',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`channel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_column
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_column`;
CREATE TABLE `sunshine_column` (
  `column_id` int(10) NOT NULL AUTO_INCREMENT,
  `channel_id` int(10) NOT NULL,
  `column_uri` varchar(255) NOT NULL,
  `column_name_cn` varchar(255) NOT NULL,
  `column_name_en` varchar(255) NOT NULL,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_comment
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_comment`;
CREATE TABLE `sunshine_comment` (
  `comment_id` bigint(30) NOT NULL AUTO_INCREMENT,
  `comment_user_id` int(30) NOT NULL,
  `comment_on_bin_id` int(30) NOT NULL,
  `comment_on_bin_type` int(4) NOT NULL,
  `comment_content` longtext NOT NULL,
  `comment_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`comment_id`),
  KEY `index_comment_bin` (`comment_on_bin_id`,`comment_on_bin_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sunshine_degree
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_degree`;
CREATE TABLE `sunshine_degree` (
  `degree_id` int(10) NOT NULL,
  `degree_name_cn` varchar(255) DEFAULT NULL,
  `degree_name_en` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`degree_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_history_log
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_history_log`;
CREATE TABLE `sunshine_history_log` (
  `log_id` bigint(32) NOT NULL AUTO_INCREMENT,
  `log_type` int(10) NOT NULL,
  `oper_user_id` int(30) NOT NULL,
  `oper_bin_id` int(30) NOT NULL,
  `ext` longtext,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sunshine_option
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_option`;
CREATE TABLE `sunshine_option` (
  `option_key` varchar(255) NOT NULL,
  `option_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`option_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_role_permission_relation
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_role_permission_relation`;
CREATE TABLE `sunshine_role_permission_relation` (
  `user_role` varchar(255) NOT NULL,
  `permission` varchar(255) NOT NULL,
  PRIMARY KEY (`user_role`,`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_user
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user`;
CREATE TABLE `sunshine_user` (
  `user_id` int(30) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(10) NOT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `user_role` int(10) DEFAULT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `password` varchar(64) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `office` varchar(255) DEFAULT NULL,
  `mobile_phone` varchar(12) DEFAULT NULL,
  `office_phone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sunshine_user_bin_behavior
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user_bin_behavior`;
CREATE TABLE `sunshine_user_bin_behavior` (
  `user|_id` int(20) NOT NULL,
  `bin_id` int(30) NOT NULL,
  `type` int(4) NOT NULL,
  `create_date` datetime NOT NULL,
  `ext` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user|_id`,`bin_id`,`type`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user|_id`) REFERENCES `sunshine_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sunshine_user_education
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user_education`;
CREATE TABLE `sunshine_user_education` (
  `e_id` int(30) NOT NULL AUTO_INCREMENT,
  `user_id` int(30) NOT NULL,
  `degree` int(10) NOT NULL,
  `major` varchar(255) DEFAULT NULL,
  `school` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `note` longtext,
  PRIMARY KEY (`e_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_user_paper
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user_paper`;
CREATE TABLE `sunshine_user_paper` (
  `user_id` int(30) NOT NULL,
  `paper_name` varchar(1024) DEFAULT NULL,
  `paper_link` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_user_project
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user_project`;
CREATE TABLE `sunshine_user_project` (
  `user_id` int(30) NOT NULL,
  `project_name` varchar(1024) DEFAULT NULL,
  `project_type` varchar(255) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `end_date` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sunshine_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `sunshine_user_role_relation`;
CREATE TABLE `sunshine_user_role_relation` (
  `user_id` int(30) NOT NULL,
  `user_role` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`,`user_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
