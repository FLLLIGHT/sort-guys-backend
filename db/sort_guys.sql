-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2021-06-19 17:38:11
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
  `name` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `updateTime` datetime NOT NULL,
  `url` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `garbage`
--

INSERT INTO `garbage` (`gid`, `name`, `type`, `description`, `updateTime`, `url`) VALUES
(1, '餐巾纸', '干垃圾', '一张普通的餐巾纸。没什么特别的，不过很常见。', '2021-05-20 17:45:00', '/garbage/napkin.jpg'),
(2, '头发', '干垃圾', '满地都是头发。只有我的头上没有。', '2021-05-20 17:45:00', '/garbage/hair.jpg'),
(3, '口香糖', '干垃圾', '平淡无奇的口香糖。', '2021-05-20 17:45:00', '/garbage/chewing_gum.jpg'),
(4, '外卖餐盒', '干垃圾', '再忙也要记得按时吃饭呀。', '2021-05-20 17:45:00', '/garbage/take-out_box.png'),
(5, '椰子壳', '干垃圾', '左半边还是右半边？其实都无所谓啦。', '2021-05-20 17:45:00', '/garbage/coconut_shell.jpg'),
(6, '口红', '干垃圾', '一支口红。是因为过期还是别的原因被丢掉的就不得而知了。', '2021-05-20 17:45:00', '/garbage/lipstick.jpg'),
(7, '西瓜皮', '湿垃圾', '还好没有直接踩上去。', '2021-05-20 17:45:00', '/garbage/watermelon_peel.jpg'),
(8, '鸡蛋壳', '湿垃圾', '鸡蛋的壳。', '2021-05-20 17:45:00', '/garbage/egg_shell.jpg'),
(9, '过期的薯片', '湿垃圾', '夜宵时间到了。等等，那包薯片过期了。', '2021-05-20 17:45:00', '/garbage/chips.jpg'),
(10, '未吃完的饭菜', '湿垃圾', '好吃?不好吃。', '2021-05-20 17:45:00', '/garbage/leftovers.jpg'),
(11, '花', '湿垃圾', '一束花。它盛开的样子很美呢。', '2021-05-20 17:45:00', '/garbage/flower.jpg'),
(12, '废电池', '有害垃圾', '一块废旧电池。', '2021-05-20 17:45:00', '/garbage/battery.jpg'),
(13, '过期药物', '有害垃圾', '过期啦！过期啦！过期啦！重要的事情说三遍。', '2021-05-20 17:45:00', '/garbage/medicine.jpg'),
(14, '水银温度计', '有害垃圾', '测体温用的水银温度计。', '2021-05-20 17:45:00', '/garbage/clinical_thermometer.jpg'),
(15, '空药瓶', '有害垃圾', '空空的药瓶。空空是谁？', '2021-05-20 17:45:00', '/garbage/medicine_bottle.jpg'),
(16, '矿泉水瓶', '可回收物', '没有水的矿泉水瓶。', '2021-05-20 17:45:00', '/garbage/water_bottle.jpg'),
(17, '牛奶盒', '可回收物', '一个牛奶盒，牛奶已经都被喝掉啦。', '2021-05-20 17:45:00', '/garbage/milk_bottle.jpg'),
(18, '易拉罐', '可回收物', '看上去很有质感，不知道味道怎么样。', '2021-05-20 17:45:00', '/garbage/can.jpg'),
(19, '装快递的纸板箱', '可回收物', '一个快递盒！是寄给谁的礼物呢？', '2021-05-20 17:45:00', '/garbage/express_box.jpg'),
(20, '操作系统的课本', '可回收物', '别扔掉啊喂！', '2021-05-20 17:45:00', '/garbage/book.jpg'),
(21, '碎玻璃', '可回收物', '玻璃水杯的碎片。记得先包起来以免划伤哦。', '2021-05-20 17:45:00', '/garbage/glass.jpg'),
(22, '毛绒玩具', '可回收物', '很可爱的毛绒玩具。不要的话可以送给我。', '2021-05-20 17:45:00', '/garbage/stuffed_toy.jpg');

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
(1, 7, 0, 0, NULL),
(2, 1, 0, 0, NULL),
(2, 2, 0, 0, NULL),
(2, 3, 0, 0, NULL),
(2, 7, 0, 0, NULL),
(3, 1, 0, 0, NULL),
(3, 2, 0, 0, NULL),
(3, 3, 0, 0, NULL),
(3, 7, 0, 0, NULL),
(4, 1, 0, 0, NULL),
(4, 2, 0, 0, NULL),
(4, 3, 0, 0, NULL),
(4, 7, 0, 0, NULL),
(5, 1, 0, 0, NULL),
(5, 2, 0, 0, NULL),
(5, 3, 0, 0, NULL),
(5, 7, 0, 0, NULL),
(6, 1, 0, 0, NULL),
(6, 2, 0, 0, NULL),
(6, 3, 0, 0, NULL),
(6, 7, 0, 0, NULL),
(7, 1, 0, 0, NULL),
(7, 2, 0, 0, NULL),
(7, 3, 0, 0, NULL),
(7, 7, 0, 0, NULL),
(8, 1, 0, 0, NULL),
(8, 2, 0, 0, NULL),
(8, 3, 0, 0, NULL),
(8, 7, 0, 0, NULL),
(9, 1, 0, 0, NULL),
(9, 2, 0, 0, NULL),
(9, 3, 0, 0, NULL),
(9, 7, 0, 0, NULL),
(10, 1, 0, 0, NULL),
(10, 2, 0, 0, NULL),
(10, 3, 0, 0, NULL),
(10, 7, 0, 0, NULL),
(11, 1, 0, 0, NULL),
(11, 2, 0, 0, NULL),
(11, 3, 0, 0, NULL),
(11, 7, 0, 0, NULL),
(12, 1, 0, 0, NULL),
(12, 2, 0, 0, NULL),
(12, 3, 0, 0, NULL),
(12, 7, 0, 0, NULL),
(13, 1, 0, 0, NULL),
(13, 2, 0, 0, NULL),
(13, 3, 0, 0, NULL),
(13, 7, 0, 0, NULL),
(14, 1, 0, 0, NULL),
(14, 2, 0, 0, NULL),
(14, 3, 0, 0, NULL),
(14, 7, 0, 0, NULL),
(15, 1, 0, 0, NULL),
(15, 2, 0, 0, NULL),
(15, 3, 0, 0, NULL),
(15, 7, 0, 0, NULL),
(16, 1, 0, 0, NULL),
(16, 2, 0, 0, NULL),
(16, 3, 0, 0, NULL),
(16, 7, 0, 0, NULL),
(17, 1, 0, 0, NULL),
(17, 2, 0, 0, NULL),
(17, 3, 0, 0, NULL),
(17, 7, 0, 0, NULL),
(18, 1, 0, 0, NULL),
(18, 2, 0, 0, NULL),
(18, 3, 0, 0, NULL),
(18, 7, 0, 0, NULL),
(19, 1, 0, 0, NULL),
(19, 2, 0, 0, NULL),
(19, 3, 0, 0, NULL),
(19, 7, 0, 0, NULL),
(20, 1, 0, 0, NULL),
(20, 2, 0, 0, NULL),
(20, 3, 0, 0, NULL),
(20, 7, 0, 0, NULL),
(21, 1, 0, 0, NULL),
(21, 2, 0, 0, NULL),
(21, 3, 0, 0, NULL),
(21, 7, 0, 0, NULL),
(22, 1, 0, 0, NULL),
(22, 2, 0, 0, NULL),
(22, 3, 0, 0, NULL),
(22, 7, 0, 0, NULL);

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
(3, 'Bob', '$2a$10$.e6FOBKAtWygZrGdPlrJiOHfAZH/F68C4rPH714aQRo.bF.s2uylq'),
(7, 'Cindy', '$2a$10$LnISb5fz0l2nAKRyFOBV7eidkaUuOoWknjyxyEbrddMd5XL2IHUoi');

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
(1, 'orange', 'models/au_blue'),
(2, 'orange', 'models/au_blue'),
(3, 'blue', 'models/au_blue'),
(7, 'blue', 'models/au_blue');

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
(3, 1, 2),
(8, 1, 7);

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
(43, 7, 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDaW5keSIsImlhdCI6MTYyNDExNjc2OCwiZXhwIjoxNjI0MTM0NzY4fQ.vBi6abqZwiIk4Jl9Cj056rqd8sVAiv4xn1wKNjSIwyIAu_B0i7lL9BZl6nDlW3_TTDr128mm22wyqjwGA0VEpw');

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
(3, 0),
(7, 0);

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
  MODIFY `gid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

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
