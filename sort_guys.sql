-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2021-04-18 12:09:25
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
(1, 'player');

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
(1, 'Alice', '123456'),
(6, 'Bob', '$2a$10$z7d5Cty6/SgRB1R.bFTcLe4iEij4BZn0cDu/V57sK.GdNKIqkSux2');

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
(6, 1, 6);

--
-- 转储表的索引
--

--
-- 表的索引 `authority`
--
ALTER TABLE `authority`
  ADD PRIMARY KEY (`aid`);

--
-- 表的索引 `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`uid`);

--
-- 表的索引 `user_authority`
--
ALTER TABLE `user_authority`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`),
  ADD KEY `aid` (`aid`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `authority`
--
ALTER TABLE `authority`
  MODIFY `aid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- 使用表AUTO_INCREMENT `user`
--
ALTER TABLE `user`
  MODIFY `uid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 使用表AUTO_INCREMENT `user_authority`
--
ALTER TABLE `user_authority`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- 限制导出的表
--

--
-- 限制表 `user_authority`
--
ALTER TABLE `user_authority`
  ADD CONSTRAINT `aid` FOREIGN KEY (`aid`) REFERENCES `authority` (`aid`),
  ADD CONSTRAINT `uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
