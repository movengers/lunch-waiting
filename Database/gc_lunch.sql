SET FOREIGN_KEY_CHECKS=0;
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

-- ----------------------------
-- View structure for rest_likes
-- ----------------------------
CREATE ALGORITHM=UNDEFINED DEFINER=`gc_lunch`@`%` SQL SECURITY DEFINER VIEW `rest_likes` AS select `gc_lunch`.`restaurant`.`no` AS `no`,ifnull(`b`.`user_likes`,0) + `gc_lunch`.`restaurant`.`default_likes` AS `likes` from (`gc_lunch`.`restaurant` left join ((select count(0) AS `user_likes`,`gc_lunch`.`rest_likes_data`.`restaurant_no` AS `restaurant_no` from `gc_lunch`.`rest_likes_data` group by `gc_lunch`.`rest_likes_data`.`restaurant_no`)) `b` on(`gc_lunch`.`restaurant`.`no` = `b`.`restaurant_no`));
