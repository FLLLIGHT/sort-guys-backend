-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2021-06-25 09:23:44
-- 服务器版本： 10.1.39-MariaDB
-- PHP 版本： 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `sort_guys`
--

-- --------------------------------------------------------

--
-- 表的结构 `authority`
--

CREATE TABLE `authority` (
  `aid` int(11) NOT NULL,
  `authority` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `authority`
--

INSERT INTO `authority` (`aid`, `authority`) VALUES
(1, 'player'),
(2, 'admin');

-- --------------------------------------------------------

--
-- 表的结构 `emoji`
--

CREATE TABLE `emoji` (
  `id` int(11) NOT NULL,
  `name` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `emoji`
--

INSERT INTO `emoji` (`id`, `name`, `url`) VALUES
(1, '1', '/emoji/1.jpg');

-- --------------------------------------------------------

--
-- 表的结构 `garbage`
--

CREATE TABLE `garbage` (
  `gid` int(11) NOT NULL,
  `cname` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `updateTime` datetime NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  `name` varchar(256) NOT NULL,
  `valid` tinyint(1) NOT NULL DEFAULT '0',
  `rate` float NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `garbage`
--

INSERT INTO `garbage` (`gid`, `cname`, `type`, `description`, `updateTime`, `url`, `name`, `valid`, `rate`) VALUES
(1, '餐巾纸', '干垃圾', '一张普通的餐巾纸。没什么特别的，不过很常见。', '2021-05-20 17:45:00', '/garbage/napkin.jpg', 'toilet_paper', 1, 2),
(5, '椰子壳', '干垃圾', '左半边还是右半边？其实都无所谓啦。', '2021-05-20 17:45:00', '/garbage/coconut_shell.jpg', 'coconut', 1, 0.5),
(6, '口红', '干垃圾', '一支口红。是因为过期还是别的原因被丢掉的就不得而知了。', '2021-05-20 17:45:00', '/garbage/lipstick.jpg', 'lipstick', 1, 0.3),
(7, '西瓜皮', '湿垃圾', '还好没有直接踩上去。', '2021-05-20 17:45:00', '/garbage/watermelon_peel.jpg', 'watermelon', 1, 0.003),
(10, '未吃完的饭菜', '湿垃圾', '好吃?不好吃。', '2021-05-20 17:45:00', '/garbage/leftovers.jpg', 'food', 1, 0.04),
(12, '废电池', '有害垃圾', '一块废旧电池。', '2021-05-20 17:45:00', '/garbage/battery.jpg', 'battery', 1, 0.03),
(14, '水银温度计', '有害垃圾', '测体温用的水银温度计。', '2021-05-20 17:45:00', '/garbage/clinical_thermometer.jpg', 'thermometer', 1, 0.1),
(18, '易拉罐', '可回收物', '看上去很有质感，不知道味道怎么样。', '2021-05-20 17:45:00', '/garbage/can.jpg', 'can', 1, 0.01),
(21, '碎玻璃', '可回收物', '玻璃水杯的碎片。记得先包起来以免划伤哦。', '2021-05-20 17:45:00', '/garbage/glass.jpg', 'glass', 1, 0.4),
(23, '笔记本', '可回收物', '比起电脑还是更习惯手写课堂笔记。', '2021-06-25 11:35:08', '/garbage/book.jpg', 'notebook', 1, 0.05);

-- --------------------------------------------------------

--
-- 表的结构 `garbage_sort_result`
--

CREATE TABLE `garbage_sort_result` (
  `gid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `times` int(11) NOT NULL DEFAULT '0',
  `correctTimes` int(11) NOT NULL DEFAULT '0',
  `unlockTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `garbage_sort_result`
--

INSERT INTO `garbage_sort_result` (`gid`, `uid`, `times`, `correctTimes`, `unlockTime`) VALUES
(1, 1, 0, 0, NULL),
(1, 2, 0, 0, NULL),
(1, 3, 0, 0, NULL),
(6, 1, 0, 0, NULL),
(6, 2, 0, 0, NULL),
(6, 3, 0, 0, NULL),
(7, 1, 0, 0, NULL),
(7, 2, 0, 0, NULL),
(7, 3, 0, 0, NULL),
(10, 1, 0, 0, NULL),
(10, 2, 0, 0, NULL),
(10, 3, 0, 0, NULL),
(12, 1, 0, 0, NULL),
(12, 2, 0, 0, NULL),
(12, 3, 0, 0, NULL),
(14, 1, 0, 0, NULL),
(14, 2, 0, 0, NULL),
(14, 3, 0, 0, NULL),
(18, 1, 0, 0, NULL),
(18, 2, 0, 0, NULL),
(18, 3, 0, 0, NULL),
(21, 1, 0, 0, NULL),
(21, 2, 0, 0, NULL),
(21, 3, 0, 0, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `scene`
--

CREATE TABLE `scene` (
  `sid` int(11) NOT NULL,
  `minX` int(11) NOT NULL,
  `maxX` int(11) NOT NULL,
  `minY` int(11) NOT NULL,
  `maxY` int(11) NOT NULL,
  `minZ` int(11) NOT NULL,
  `maxZ` int(11) NOT NULL,
  `name` varchar(2048) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `scene`
--

INSERT INTO `scene` (`sid`, `minX`, `maxX`, `minY`, `maxY`, `minZ`, `maxZ`, `name`) VALUES
(1, -54, 54, 0, 0, -54, 54, '默认场景');

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE `user` (
  `uid` int(11) NOT NULL,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`uid`, `username`, `password`) VALUES
(1, 'Admin', '$2a$10$NM7s19sWIN4LBfBWHqkIKuaAk15j4kZndX5mqtDONmdxWseECQoxW'),
(2, 'Alice', '$2a$10$pKMujZhdPtNDKwnY8I.RjOj1N2eGe9KiNv2a/OBSv65tGxH0TdjDe'),
(3, 'Bob', '$2a$10$.e6FOBKAtWygZrGdPlrJiOHfAZH/F68C4rPH714aQRo.bF.s2uylq');

-- --------------------------------------------------------

--
-- 表的结构 `user_appearance`
--

CREATE TABLE `user_appearance` (
  `uid` int(11) NOT NULL,
  `color` varchar(128) NOT NULL,
  `url` varchar(128) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user_appearance`
--

INSERT INTO `user_appearance` (`uid`, `color`, `url`) VALUES
(1, 'blue', 'models/au_blue'),
(2, 'orange', 'models/au_orange'),
(3, 'blue', 'models/au_blue');

-- --------------------------------------------------------

--
-- 表的结构 `user_authority`
--

CREATE TABLE `user_authority` (
  `id` int(11) NOT NULL,
  `aid` int(11) NOT NULL,
  `uid` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user_authority`
--

INSERT INTO `user_authority` (`id`, `aid`, `uid`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 1, 2);

-- --------------------------------------------------------

--
-- 表的结构 `user_login_info`
--

CREATE TABLE `user_login_info` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `token` varchar(1024) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user_login_info`
--

INSERT INTO `user_login_info` (`id`, `uid`, `token`) VALUES
(45, 2, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBbGljZSIsImlhdCI6MTYyNDU0MTkxMSwiZXhwIjoxNjI0NTU5OTExfQ.ZzuNxAT7CAHn9xFgrogJ4XS3njsww8yY-BnhpzpI3qMSqnQxVp-a3mf41SmvbUjOystNiXSPPK-Dyy6Wea1MuQ');

-- --------------------------------------------------------

--
-- 表的结构 `user_score`
--

CREATE TABLE `user_score` (
  `uid` int(11) NOT NULL,
  `score` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user_score`
--

INSERT INTO `user_score` (`uid`, `score`) VALUES
(1, 0),
(2, 0),
(3, 0);

--
-- 转储表的索引
--

--
-- 表的索引 `authority`
--
ALTER TABLE `authority`
  ADD PRIMARY KEY (`aid`);

--
-- 表的索引 `emoji`
--
ALTER TABLE `emoji`
  ADD PRIMARY KEY (`id`);

--
-- 表的索引 `garbage`
--
ALTER TABLE `garbage`
  ADD PRIMARY KEY (`gid`);

--
-- 表的索引 `garbage_sort_result`
--
ALTER TABLE `garbage_sort_result`
  ADD PRIMARY KEY (`gid`,`uid`),
  ADD KEY `uid` (`uid`);

--
-- 表的索引 `scene`
--
ALTER TABLE `scene`
  ADD PRIMARY KEY (`sid`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- 表的索引 `user_appearance`
--
ALTER TABLE `user_appearance`
  ADD PRIMARY KEY (`uid`);

--
-- 表的索引 `user_authority`
--
ALTER TABLE `user_authority`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`),
  ADD KEY `aid` (`aid`);

--
-- 表的索引 `user_login_info`
--
ALTER TABLE `user_login_info`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`);

--
-- 表的索引 `user_score`
--
ALTER TABLE `user_score`
  ADD PRIMARY KEY (`uid`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `authority`
--
ALTER TABLE `authority`
  MODIFY `aid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- 使用表AUTO_INCREMENT `emoji`
--
ALTER TABLE `emoji`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `garbage`
--
ALTER TABLE `garbage`
  MODIFY `gid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;

--
-- 使用表AUTO_INCREMENT `scene`
--
ALTER TABLE `scene`
  MODIFY `sid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用表AUTO_INCREMENT `user_authority`
--
ALTER TABLE `user_authority`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- 使用表AUTO_INCREMENT `user_login_info`
--
ALTER TABLE `user_login_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=46;

--
-- 限制导出的表
--

--
-- 限制表 `garbage_sort_result`
--
ALTER TABLE `garbage_sort_result`
  ADD CONSTRAINT `garbage_sort_result_ibfk_1` FOREIGN KEY (`gid`) REFERENCES `garbage` (`gid`),
  ADD CONSTRAINT `garbage_sort_result_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

--
-- 限制表 `user_appearance`
--
ALTER TABLE `user_appearance`
  ADD CONSTRAINT `user_appearance_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

--
-- 限制表 `user_authority`
--
ALTER TABLE `user_authority`
  ADD CONSTRAINT `user_authority_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  ADD CONSTRAINT `user_authority_ibfk_2` FOREIGN KEY (`aid`) REFERENCES `authority` (`aid`);

--
-- 限制表 `user_login_info`
--
ALTER TABLE `user_login_info`
  ADD CONSTRAINT `user_login_info_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);

--
-- 限制表 `user_score`
--
ALTER TABLE `user_score`
  ADD CONSTRAINT `user_score_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
