/*
Navicat MySQL Data Transfer

Source Server         : local
Source Server Version : 50722
Source Host           : 127.0.0.1:3306
Source Database       : other

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-06-03 17:45:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for crawl_error
-- ----------------------------
DROP TABLE IF EXISTS `crawl_error`;
CREATE TABLE `crawl_error` (
  `id` int(30) NOT NULL AUTO_INCREMENT,
  `site` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT NULL,
  `deleted` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `sno` varchar(56) NOT NULL,
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
-- Table structure for weibo
-- ----------------------------
DROP TABLE IF EXISTS `weibo`;
CREATE TABLE `weibo` (
  `w_ouid` varchar(30) NOT NULL,
  `w_mid` varchar(30) NOT NULL,
  `w_user_name` varchar(255) DEFAULT NULL,
  `w_url` varchar(255) DEFAULT NULL,
  `w_date` varchar(20) DEFAULT NULL,
  `w_from` varchar(255) DEFAULT NULL,
  `w_share_count` varchar(255) DEFAULT NULL,
  `w_comment_count` varchar(255) DEFAULT NULL,
  `w_thumb_count` varchar(255) DEFAULT NULL,
  `w_content` varchar(4096) DEFAULT NULL,
  `w_pics` varchar(1024) DEFAULT NULL,
  `crawl_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `index_words` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`w_mid`)
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
-- Table structure for weibo_user_config
-- ----------------------------
DROP TABLE IF EXISTS `weibo_user_config`;
CREATE TABLE `weibo_user_config` (
  `oid` varchar(255) NOT NULL,
  `uri` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `domain` varchar(255) NOT NULL,
  `pids` varchar(255) NOT NULL,
  `weibo_num` varchar(255) NOT NULL,
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
