/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : other

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2019-04-14 00:46:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawl_error
-- ----------------------------
DROP TABLE IF EXISTS `crawl_error`;
CREATE TABLE `crawl_error` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `site` varchar(255) NOT NULL,
  `url` varchar(1024) NOT NULL,
  `method` varchar(10) NOT NULL,
  `body` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `error` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=354 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for cssci_author
-- ----------------------------
DROP TABLE IF EXISTS `cssci_author`;
CREATE TABLE `cssci_author` (
  `bmmc` varchar(255) DEFAULT NULL,
  `dcbj` varchar(255) DEFAULT NULL,
  `fwxs` varchar(255) DEFAULT NULL,
  `id` varchar(30) NOT NULL,
  `jglb1` varchar(255) DEFAULT NULL,
  `jgmc` varchar(255) DEFAULT NULL,
  `pyxm` varchar(255) DEFAULT NULL,
  `sftt` varchar(255) DEFAULT NULL,
  `sxdm` varchar(255) DEFAULT NULL,
  `txdz` varchar(255) DEFAULT NULL,
  `yzbm` varchar(255) DEFAULT NULL,
  `zzdq` varchar(255) DEFAULT NULL,
  `zzpm` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for cssci_citation
-- ----------------------------
DROP TABLE IF EXISTS `cssci_citation`;
CREATE TABLE `cssci_citation` (
  `id` varchar(255) NOT NULL,
  `sno` varchar(60) NOT NULL,
  `by_tag` varchar(10) DEFAULT NULL,
  `bynd` varchar(20) DEFAULT NULL,
  `dcbj` varchar(255) DEFAULT NULL,
  `jcxh` varchar(255) DEFAULT NULL,
  `qk_tag` varchar(10) DEFAULT NULL,
  `qkdm` varchar(255) DEFAULT NULL,
  `qkno` varchar(255) DEFAULT NULL,
  `wzdm` varchar(10) DEFAULT NULL,
  `yw_id` varchar(60) DEFAULT NULL,
  `ywcbd` varchar(255) DEFAULT NULL,
  `ywcbs` varchar(255) DEFAULT NULL,
  `ywcc` varchar(255) DEFAULT NULL,
  `ywlx` varchar(255) DEFAULT NULL,
  `ywnd` varchar(20) DEFAULT NULL,
  `ywno` varchar(20) DEFAULT NULL,
  `ywpm` varchar(1024) DEFAULT NULL,
  `ywpm_p` varchar(1024) DEFAULT NULL,
  `ywqk` varchar(512) DEFAULT NULL,
  `ywqk_id` varchar(10) DEFAULT NULL,
  `ywxj` varchar(255) DEFAULT NULL,
  `ywym` varchar(255) DEFAULT NULL,
  `ywyz` varchar(255) DEFAULT NULL,
  `ywzz` varchar(255) DEFAULT NULL,
  `ywzz_p` varchar(255) DEFAULT NULL,
  `yylb` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`,`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for cssci_journal
-- ----------------------------
DROP TABLE IF EXISTS `cssci_journal`;
CREATE TABLE `cssci_journal` (
  `id` varchar(255) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `creat_pubdate` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gg` varchar(255) DEFAULT NULL,
  `hx` varchar(255) DEFAULT NULL,
  `issn` varchar(255) DEFAULT NULL,
  `kb` varchar(255) DEFAULT NULL,
  `lan` varchar(255) DEFAULT NULL,
  `old_name` varchar(255) DEFAULT NULL,
  `post` varchar(255) DEFAULT NULL,
  `press` varchar(255) DEFAULT NULL,
  `qkType` varchar(255) DEFAULT NULL,
  `qk_img` varchar(255) DEFAULT NULL,
  `tag` varchar(255) DEFAULT NULL,
  `tel` varchar(255) DEFAULT NULL,
  `title_c` varchar(255) DEFAULT NULL,
  `title_e` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `xk_id` varchar(255) DEFAULT NULL,
  `xn` varchar(255) DEFAULT NULL,
  `zq` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for cssci_paper
-- ----------------------------
DROP TABLE IF EXISTS `cssci_paper`;
CREATE TABLE `cssci_paper` (
  `sno` varchar(56) NOT NULL,
  `authors` varchar(1024) DEFAULT NULL,
  `authors_id` varchar(512) DEFAULT NULL,
  `authors_address` longtext,
  `authors_jg` varchar(2048) DEFAULT NULL,
  `blpm` varchar(1024) DEFAULT NULL,
  `byc` varchar(1024) DEFAULT NULL,
  `dcbj` varchar(255) DEFAULT NULL,
  `id` varchar(255) DEFAULT NULL,
  `jjlb` varchar(255) DEFAULT NULL,
  `juan` varchar(255) DEFAULT NULL,
  `lrymc` varchar(255) DEFAULT NULL,
  `lypm` varchar(1024) DEFAULT NULL,
  `lypmp` varchar(1024) DEFAULT NULL,
  `nian` varchar(255) DEFAULT NULL,
  `qi` varchar(255) DEFAULT NULL,
  `qkdm` varchar(32) DEFAULT NULL,
  `qkmc` varchar(255) DEFAULT NULL,
  `qkno` varchar(32) DEFAULT NULL,
  `skdm` varchar(255) DEFAULT NULL,
  `wzlx` varchar(10) DEFAULT NULL,
  `wzlx_z` varchar(10) DEFAULT NULL,
  `xkdm1` varchar(20) DEFAULT NULL,
  `xkdm2` varchar(20) DEFAULT NULL,
  `xkfl1` varchar(10) DEFAULT NULL,
  `xkfl2` varchar(10) DEFAULT NULL,
  `xmlb` varchar(255) DEFAULT NULL,
  `ycflag` varchar(10) DEFAULT NULL,
  `yjdm` varchar(30) DEFAULT NULL,
  `ym` varchar(10) DEFAULT NULL,
  `ywsl` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for live_platform_policy
-- ----------------------------
DROP TABLE IF EXISTS `live_platform_policy`;
CREATE TABLE `live_platform_policy` (
  `url` varchar(255) NOT NULL,
  `pub_date` varchar(255) DEFAULT NULL,
  `content` longtext,
  PRIMARY KEY (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `userno` varchar(20) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

-- ----------------------------
-- Table structure for weibo_changshuhan
-- ----------------------------
DROP TABLE IF EXISTS `weibo_changshuhan`;
CREATE TABLE `weibo_changshuhan` (
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
  `index_words` varchar(1024) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `full_content_param` longtext,
  PRIMARY KEY (`w_url`),
  KEY `index_uid` (`w_ouid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for weibo_comment
-- ----------------------------
DROP TABLE IF EXISTS `weibo_comment`;
CREATE TABLE `weibo_comment` (
  `w_mid` varchar(30) DEFAULT NULL,
  `w_ouid` varchar(30) DEFAULT NULL,
  `c_user_name` varchar(255) DEFAULT NULL,
  `c_date` varchar(20) DEFAULT NULL,
  `c_content` varchar(512) DEFAULT NULL,
  `c_user_url` varchar(255) DEFAULT NULL,
  `c_reply` varchar(20) DEFAULT NULL,
  `c_thumb` varchar(20) DEFAULT NULL,
  `c_crawl_date` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for weibo_qubo_1
-- ----------------------------
DROP TABLE IF EXISTS `weibo_qubo_1`;
CREATE TABLE `weibo_qubo_1` (
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
  `index_words` varchar(1024) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `full_content_param` longtext,
  PRIMARY KEY (`w_url`),
  KEY `index_uid` (`w_ouid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

-- ----------------------------
-- Table structure for weibo_user_config_changshuhan
-- ----------------------------
DROP TABLE IF EXISTS `weibo_user_config_changshuhan`;
CREATE TABLE `weibo_user_config_changshuhan` (
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
