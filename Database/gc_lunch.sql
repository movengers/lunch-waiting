SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for board
-- ----------------------------
CREATE TABLE `board` (
  `no` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `content` text NOT NULL,
  `parent_no` int(11) DEFAULT NULL,
  `time` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`no`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
CREATE TABLE `menu` (
  `restaurant_no` int(11) NOT NULL,
  `priority` int(11) NOT NULL,
  `name` char(50) NOT NULL DEFAULT '',
  `price` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`restaurant_no`,`priority`),
  CONSTRAINT `menu_ibfk_1` FOREIGN KEY (`restaurant_no`) REFERENCES `restaurant` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rest_comment
-- ----------------------------
CREATE TABLE `rest_comment` (
  `restaurant_no` int(11) NOT NULL,
  `user_no` int(11) NOT NULL,
  `comment` text NOT NULL,
  `date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rest_likes_data
-- ----------------------------
CREATE TABLE `rest_likes_data` (
  `user_id` int(11) NOT NULL,
  `restaurant_no` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`restaurant_no`),
  KEY `restaurant_no` (`restaurant_no`),
  CONSTRAINT `rest_likes_data_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rest_likes_data_ibfk_2` FOREIGN KEY (`restaurant_no`) REFERENCES `restaurant` (`no`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
CREATE TABLE `restaurant` (
  `no` int(11) NOT NULL,
  `title` char(50) NOT NULL,
  `category` char(40) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `roadAddress` char(60) DEFAULT '',
  `image` varchar(100) DEFAULT NULL,
  `mapx` double DEFAULT NULL,
  `mapy` double DEFAULT NULL,
  `computed_waiting` int(11) DEFAULT NULL,
  `default_likes` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`no`),
  UNIQUE KEY `name` (`title`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for waiting_data
-- ----------------------------
CREATE TABLE `waiting_data` (
  `restaurant_no` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `waiting` int(11) NOT NULL,
  KEY `restaurant_no` (`restaurant_no`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `waiting_data_ibfk_1` FOREIGN KEY (`restaurant_no`) REFERENCES `restaurant` (`no`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `waiting_data_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- View structure for board_writer
-- ----------------------------
CREATE ALGORITHM=UNDEFINED DEFINER=`banksemi`@`%` SQL SECURITY DEFINER VIEW `board_writer` AS select `board`.`no` AS `no`,`board`.`user_id` AS `user_id`,`board`.`content` AS `content`,`board`.`parent_no` AS `parent_no`,`user`.`name` AS `name`,`board`.`time` AS `time` from (`board` join `user` on(`user`.`id` = `board`.`user_id`));
