/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : osf

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-02-03 18:30:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for osf_albums
-- ----------------------------
DROP TABLE IF EXISTS `osf_albums`;
CREATE TABLE `osf_albums` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `album_title` text,
  `album_desc` text COMMENT '描述',
  `last_add_ts` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `photos_count` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `cover` varchar(45) DEFAULT NULL,
  `album_tags` text,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_osf_albums_album_author_idx` (`user_id`),
  CONSTRAINT `fk_osf_albums_album_author` FOREIGN KEY (`user_id`) REFERENCES `osf_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_albums
-- ----------------------------

-- ----------------------------
-- Table structure for osf_comments
-- ----------------------------
DROP TABLE IF EXISTS `osf_comments`;
CREATE TABLE `osf_comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment_object_type` int(11) NOT NULL COMMENT 'post, album,...',
  `comment_object_id` int(11) NOT NULL,
  `comment_author` int(11) NOT NULL,
  `comment_author_name` varchar(100) NOT NULL,
  `comment_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `comment_content` text NOT NULL,
  `comment_parent` int(11) NOT NULL DEFAULT '0',
  `comment_parent_author_name` varchar(100) DEFAULT NULL,
  `comment_parent_author` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_osf_comments_comment_author_idx` (`comment_author`),
  CONSTRAINT `fk_osf_comments_comment_author` FOREIGN KEY (`comment_author`) REFERENCES `osf_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_comments
-- ----------------------------
INSERT INTO `osf_comments` VALUES ('1', '4', '4', '1', '泉水', '2018-02-01 16:26:45', '呦，还可以评论啊', '0', null, '0', '2018-02-01 17:18:51', '2018-02-01 17:18:51');

-- ----------------------------
-- Table structure for osf_events
-- ----------------------------
DROP TABLE IF EXISTS `osf_events`;
CREATE TABLE `osf_events` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `user_avatar` varchar(100) DEFAULT NULL,
  `like_count` int(11) NOT NULL,
  `share_count` int(11) NOT NULL,
  `comment_count` int(11) NOT NULL,
  `title` text,
  `summary` text,
  `content` text,
  `tags` text,
  `following_user_id` int(11) DEFAULT NULL,
  `following_user_name` varchar(50) DEFAULT NULL,
  `follower_user_id` int(11) DEFAULT NULL,
  `follower_user_name` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_events
-- ----------------------------
INSERT INTO `osf_events` VALUES ('1', '4', '2', '2018-02-01 15:38:29', '1', null, null, '0', '0', '0', null, '你好，', null, null, '0', null, '0', null, '2018-02-01 17:18:52', '2018-02-01 17:18:52');
INSERT INTO `osf_events` VALUES ('2', '0', '3', '2018-02-01 15:43:46', '1', null, null, '0', '0', '0', '2018.2.1', '生活不止眼前的苟且，还有诗和远方的田野', null, '测试:1 ', '0', null, '0', null, '2018-02-01 17:18:52', '2018-02-01 17:18:52');
INSERT INTO `osf_events` VALUES ('3', '4', '4', '2018-02-01 16:26:12', '1', null, null, '0', '0', '0', null, '我系渣渣灰<img class=\"emojione\" alt=\"\" src=\"/osf/img/emoji/1F605.png?v=1.2.4\"/>', null, null, '0', null, '0', null, '2018-02-01 17:18:52', '2018-02-01 17:18:52');

-- ----------------------------
-- Table structure for osf_followers
-- ----------------------------
DROP TABLE IF EXISTS `osf_followers`;
CREATE TABLE `osf_followers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `follower_user_id` int(11) NOT NULL,
  `follower_user_name` varchar(50) DEFAULT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`follower_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_followers
-- ----------------------------

-- ----------------------------
-- Table structure for osf_followings
-- ----------------------------
DROP TABLE IF EXISTS `osf_followings`;
CREATE TABLE `osf_followings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `following_user_id` int(11) NOT NULL,
  `following_user_name` varchar(50) DEFAULT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`following_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_followings
-- ----------------------------

-- ----------------------------
-- Table structure for osf_interests
-- ----------------------------
DROP TABLE IF EXISTS `osf_interests`;
CREATE TABLE `osf_interests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_interests
-- ----------------------------

-- ----------------------------
-- Table structure for osf_likes
-- ----------------------------
DROP TABLE IF EXISTS `osf_likes`;
CREATE TABLE `osf_likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`object_type`,`object_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_likes
-- ----------------------------

-- ----------------------------
-- Table structure for osf_notifications
-- ----------------------------
DROP TABLE IF EXISTS `osf_notifications`;
CREATE TABLE `osf_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notify_type` int(11) NOT NULL,
  `notify_id` int(11) NOT NULL,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `notified_user` int(11) NOT NULL,
  `notifier` int(11) NOT NULL,
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_notifications
-- ----------------------------
INSERT INTO `osf_notifications` VALUES ('1', '4', '0', '5', '1', '1', '2', '2018-02-01 15:46:56', '0', '2018-02-01 17:18:57', '2018-02-01 17:18:57');
INSERT INTO `osf_notifications` VALUES ('2', '1', '1', '4', '4', '1', '1', '2018-02-01 16:26:45', '0', '2018-02-01 17:18:57', '2018-02-01 17:18:57');

-- ----------------------------
-- Table structure for osf_photos
-- ----------------------------
DROP TABLE IF EXISTS `osf_photos`;
CREATE TABLE `osf_photos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(45) NOT NULL,
  `album_id` int(11) NOT NULL,
  `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `desc` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_photos
-- ----------------------------

-- ----------------------------
-- Table structure for osf_posts
-- ----------------------------
DROP TABLE IF EXISTS `osf_posts`;
CREATE TABLE `osf_posts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `post_author` int(11) NOT NULL COMMENT '作者ID',
  `post_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `post_content` longtext NOT NULL,
  `post_title` text,
  `post_excerpt` text COMMENT '摘要',
  `post_status` int(11) NOT NULL DEFAULT '0',
  `comment_status` int(11) NOT NULL DEFAULT '0',
  `post_pwd` varchar(60) DEFAULT NULL,
  `post_lastts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `comment_count` int(11) NOT NULL DEFAULT '0',
  `like_count` int(11) NOT NULL DEFAULT '0',
  `share_count` int(11) NOT NULL DEFAULT '0',
  `post_url` varchar(45) DEFAULT NULL,
  `post_tags` text,
  `post_album` int(11) NOT NULL DEFAULT '0',
  `post_cover` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_osf_users_post_author_idx` (`post_author`),
  CONSTRAINT `fk_osf_users_post_author` FOREIGN KEY (`post_author`) REFERENCES `osf_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_posts
-- ----------------------------
INSERT INTO `osf_posts` VALUES ('1', '1', '2018-02-01 14:28:36', '生活不止眼前的苟且，还有诗和远方的田野', null, null, '0', '0', null, '2018-02-01 14:28:36', '0', '0', '0', null, null, '0', null, '2018-02-01 17:18:59', '2018-02-01 17:18:59');
INSERT INTO `osf_posts` VALUES ('2', '1', '2018-02-01 15:38:29', '你好，', null, null, '0', '0', null, '2018-02-01 15:38:29', '0', '0', '0', null, null, '0', null, '2018-02-01 17:18:59', '2018-02-01 17:18:59');
INSERT INTO `osf_posts` VALUES ('3', '1', '2018-02-01 15:43:46', '<p>生活不止眼前的苟且，还有诗和远方的田野</p>', '2018.2.1', '生活不止眼前的苟且，还有诗和远方的田野', '0', '0', null, '2018-02-01 15:43:46', '0', '0', '0', null, null, '0', null, '2018-02-01 17:18:59', '2018-02-01 17:18:59');
INSERT INTO `osf_posts` VALUES ('4', '1', '2018-02-01 16:26:12', '我系渣渣灰<img class=\"emojione\" alt=\"\" src=\"/osf/img/emoji/1F605.png?v=1.2.4\"/>', null, null, '0', '0', null, '2018-02-01 16:26:12', '0', '0', '0', null, null, '0', null, '2018-02-01 17:18:59', '2018-02-01 17:18:59');

-- ----------------------------
-- Table structure for osf_relations
-- ----------------------------
DROP TABLE IF EXISTS `osf_relations`;
CREATE TABLE `osf_relations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `object_type` int(11) NOT NULL,
  `object_id` int(11) NOT NULL,
  `tag_id` int(11) NOT NULL,
  `add_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_tag_id_idx` (`tag_id`),
  CONSTRAINT `fk_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `osf_tags` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_relations
-- ----------------------------
INSERT INTO `osf_relations` VALUES ('1', '0', '3', '1', '2018-02-01 15:43:46', '2018-02-01 17:18:59', '2018-02-01 17:18:59');

-- ----------------------------
-- Table structure for osf_tags
-- ----------------------------
DROP TABLE IF EXISTS `osf_tags`;
CREATE TABLE `osf_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(30) NOT NULL,
  `add_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `cover` varchar(45) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_tags
-- ----------------------------
INSERT INTO `osf_tags` VALUES ('1', '测试', '2018-02-01 15:43:45', null, '2018-02-01 17:19:00', '2018-02-01 17:19:00');

-- ----------------------------
-- Table structure for osf_users
-- ----------------------------
DROP TABLE IF EXISTS `osf_users`;
CREATE TABLE `osf_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL,
  `user_email` varchar(100) NOT NULL,
  `user_pwd` varchar(100) NOT NULL,
  `user_registered_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_status` int(11) DEFAULT NULL,
  `user_activationKey` varchar(24) DEFAULT NULL,
  `user_avatar` varchar(100) DEFAULT NULL,
  `user_desc` text,
  `resetpwd_key` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`,`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of osf_users
-- ----------------------------
INSERT INTO `osf_users` VALUES ('1', '泉水', 'songdongliang111@gmail.com', 'ED359AAE0884567C73201D519D7857FB', '2018-01-31 15:03:50', '0', 'ROXjRu1dwyOQiqSrtK7qBg==', 'group1/M00/00/00/wKgqgFpysxWABESaAAAhEkq_pOs80.jpeg', null, null, '2018-02-01 17:08:03', '2018-02-01 17:20:05');
INSERT INTO `osf_users` VALUES ('2', '演员', 'songdongliang111@qq.com', 'ED359AAE0884567C73201D519D7857FB', '2018-02-01 15:44:54', '0', '9AlVsQanUTu9Yb7NxazDDA==', '173ur96139.iok.lagroup1/M00/00/00/wKgqgFqSSv6AXAcDAACA5C9vxNM384.jpg', null, null, '2018-02-01 17:08:03', '2018-02-01 17:20:05');
