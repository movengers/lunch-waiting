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