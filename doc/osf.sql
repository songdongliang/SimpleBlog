
use osf;

drop table if EXISTS `osf`.`osf_users`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(50) NULL,
  `userEmail` VARCHAR(100) NOT NULL,
  `userPwd` VARCHAR(100) NOT NULL,
  `userRegisteredDate` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `userStatus` INT NULL,
  `userActivationKey` VARCHAR(24) NULL,
  `userAvatar` VARCHAR(100) null,
  `userDesc` TEXT null,
  `resetpwd_key` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
alter table `osf_users` add unique(`userName`, `userEmail`);


drop table if EXISTS `osf`.`osf_posts`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_posts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `postAuthor` INT NOT NULL COMMENT '作者ID',
  `postTs` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `postContent` LONGTEXT NOT NULL,
  `postTitle` TEXT  NULL,
  `postExcerpt` TEXT NULL COMMENT '摘要',
  `postStatus` INT NOT NULL DEFAULT 0,
  `commentStatus` INT NOT NULL DEFAULT 0,
  `postPwd` VARCHAR(60) NULL,
  `postLastts` TIMESTAMP NOT NULL DEFAULT current_timestamp on update CURRENT_TIMESTAMP,
  `commentCount` INT NOT NULL DEFAULT 0,
  `likeCount` INT NOT NULL DEFAULT 0,
  `shareCount` INT NOT NULL DEFAULT 0,
  `postUrl` VARCHAR(45) NULL,
  `postTags` TEXT NULL,
  `postAlbum` INT NOT NULL DEFAULT 0,
  `postCover` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_osf_users_post_author_idx` (`postAuthor` ASC),
  CONSTRAINT `fk_osf_users_post_author`
    FOREIGN KEY (`postAuthor`)
    REFERENCES `osf`.`osf_users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


drop table if EXISTS `osf`.`osf_comments`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `commentObjectType` INT NOT NULL COMMENT 'post, album,...',
  `commentObjectId` INT NOT NULL,
  `commentAuthor` INT NOT NULL,
  `commentAuthorName` VARCHAR(100) NOT NULL,
  `commentTs` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `commentContent` TEXT NOT NULL,
  `commentParent` INT NOT NULL DEFAULT 0,
  `commentParentAuthorName` VARCHAR(100) NULL,
  `commentParentAuthor` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_osf_comments_comment_author_idx` (`commentAuthor` ASC),
  CONSTRAINT `fk_osf_comments_comment_author`
    FOREIGN KEY (`commentAuthor`)
    REFERENCES `osf`.`osf_users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


drop table if EXISTS `osf`.`osf_events`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_events` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `objectType` INT NOT NULL,
  `objectId` INT NOT NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `userId` INT NOT NULL,
  `userName` VARCHAR(50) NULL,
  `userAvatar` VARCHAR(100) NULL,
  `likeCount` INT NOT NULL,
  `shareCount` INT NOT NULL,
  `commentCount` INT NOT NULL,
  `title` TEXT NULL,
  `summary` TEXT NULL,
  `content` TEXT NULL,
  `tags` TEXT NULL,
  `followingUserId` INT NULL,
  `followingUserName` VARCHAR(50) NULL,
  `followerUserId` INT NULL,
  `followerUserName` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


drop table if EXISTS `osf`.`osf_followings`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_followings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `userName` VARCHAR(50) NULL,
  `followingUserId` INT NOT NULL,
  `followingUserName` VARCHAR(50) NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
alter table `osf`.`osf_followings` add unique(userId, followingUserId);


drop table if EXISTS `osf`.`osf_followers` ;
CREATE TABLE IF NOT EXISTS `osf`.`osf_followers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `userName` VARCHAR(50) NULL,
  `followerUserId` INT NOT NULL,
  `followerUserName` VARCHAR(50) NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
alter table `osf`.`osf_followers` add unique(userId, followerUserId);


drop table if EXISTS `osf`.`osf_albums`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_albums` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `createTs` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `albumTitle` TEXT NULL,
  `albumDesc` TEXT NULL COMMENT '描述',
  `lastAddTs` DATETIME NOT NULL DEFAULT current_timestamp,
  `photosCount` INT NOT NULL DEFAULT 0,
  `status` INT NOT NULL DEFAULT 0,
  `cover` VARCHAR(45) NULL,
  `albumTags` TEXT null,
  PRIMARY KEY (`id`),
  INDEX `fk_osf_albums_album_author_idx` (`userId` ASC),
  CONSTRAINT `fk_osf_albums_album_author`
    FOREIGN KEY (`userId`)
    REFERENCES `osf`.`osf_users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


drop table if EXISTS `osf`.`osf_photos`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_photos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `key` VARCHAR(45) NOT NULL,
  `albumId` INT NOT NULL,
  `ts` TIMESTAMP NULL DEFAULT current_timestamp,
  `desc` VARCHAR(50) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;



drop table if EXISTS `osf`.`osf_tags`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `tag` VARCHAR(30) NOT NULL,
  `addTs` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  `cover` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;



drop table if EXISTS `osf`.`osf_relations` ;
CREATE TABLE IF NOT EXISTS `osf`.`osf_relations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `objectType` INT NOT NULL,
  `objectId` INT NOT NULL,
  `tagId` INT NOT NULL,
  `addTs` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`),
  INDEX `fk_tag_id_idx` (`tagId` ASC),
  CONSTRAINT `fk_tag_id`
    FOREIGN KEY (`tagId`)
    REFERENCES `osf`.`osf_tags` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


drop table if EXISTS `osf`.`osf_interests` ;
CREATE TABLE IF NOT EXISTS `osf`.`osf_interests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `tagId` INT NOT NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
alter table `osf_interests` add unique(`userId`, `tagId`);


drop table if EXISTS `osf`.`osf_likes`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_likes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `objectType` INT NOT NULL,
  `objectId` INT NOT NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;
alter table `osf_likes` add unique(`userId`, `objectType`, `objectId`);


drop table if EXISTS `osf`.`osf_notifications`;
CREATE TABLE IF NOT EXISTS `osf`.`osf_notifications` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `notifyType` INT NOT NULL,
  `notifyId` INT NOT NULL,
  `objectType` INT NOT NULL,
  `objectId` INT NOT NULL,
  `notifiedUser` INT NOT NULL,
  `notifier` INT NOT NULL,
  `ts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


