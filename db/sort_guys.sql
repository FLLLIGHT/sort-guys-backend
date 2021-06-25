-- MySQL dump 10.13  Distrib 8.0.20, for Win64 (x86_64)
--
-- Host: localhost    Database: sort_guys
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `authority`
--

DROP TABLE IF EXISTS `authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `authority` (
  `aid` int NOT NULL AUTO_INCREMENT,
  `authority` varchar(256) NOT NULL,
  PRIMARY KEY (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `authority`
--

LOCK TABLES `authority` WRITE;
/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
INSERT INTO `authority` VALUES (1,'player'),(2,'admin');
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `emoji`
--

DROP TABLE IF EXISTS `emoji`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emoji` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(256) NOT NULL,
  `url` varchar(256) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emoji`
--

LOCK TABLES `emoji` WRITE;
/*!40000 ALTER TABLE `emoji` DISABLE KEYS */;
INSERT INTO `emoji` VALUES (1,'1','/emojis/1.jpg'),(2,'2','/emojis/2.jpg'),(3,'3','/emojis/3.jpg');
/*!40000 ALTER TABLE `emoji` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garbage`
--

DROP TABLE IF EXISTS `garbage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garbage` (
  `gid` int NOT NULL AUTO_INCREMENT,
  `cname` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `updateTime` datetime NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  `name` varchar(256) NOT NULL,
  `valid` tinyint(1) NOT NULL DEFAULT '0',
  `rate` float NOT NULL DEFAULT '1',
  PRIMARY KEY (`gid`),
  CONSTRAINT `garbage_chk_1` CHECK ((`type` in (_utf8mb3'干垃圾',_utf8mb3'湿垃圾',_utf8mb3'有害垃圾',_utf8mb3'可回收物')))
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garbage`
--

LOCK TABLES `garbage` WRITE;
/*!40000 ALTER TABLE `garbage` DISABLE KEYS */;
INSERT INTO `garbage` VALUES (1,'餐巾纸','干垃圾','一张普通的餐巾纸。没什么特别的，不过很常见。','2021-05-20 17:45:00','/garbage/napkin.jpg','toilet_paper',1,2),(5,'椰子壳','干垃圾','左半边还是右半边？其实都无所谓啦。','2021-05-20 17:45:00','/garbage/coconut_shell.jpg','coconut',1,0.5),(6,'口红','干垃圾','一支口红。是因为过期还是别的原因被丢掉的就不得而知了。','2021-05-20 17:45:00','/garbage/lipstick.jpg','lipstick',1,0.3),(7,'西瓜皮','湿垃圾','还好没有直接踩上去。','2021-05-20 17:45:00','/garbage/watermelon_peel.jpg','watermelon',1,0.003),(10,'未吃完的饭菜','湿垃圾','好吃?不好吃。','2021-05-20 17:45:00','/garbage/leftovers.jpg','food',1,0.04),(12,'废电池','有害垃圾','一块废旧电池。','2021-05-20 17:45:00','/garbage/battery.jpg','battery',1,0.03),(14,'水银温度计','有害垃圾','测体温用的水银温度计。','2021-05-20 17:45:00','/garbage/clinical_thermometer.jpg','thermometer',1,0.1),(18,'易拉罐','可回收物','看上去很有质感，不知道味道怎么样。','2021-05-20 17:45:00','/garbage/can.jpg','can',1,0.01),(21,'碎玻璃','可回收物','玻璃水杯的碎片。记得先包起来以免划伤哦。','2021-05-20 17:45:00','/garbage/glass.jpg','glass',1,0.4),(23,'笔记本','可回收物','比起电脑还是更习惯手写课堂笔记。','2021-06-25 11:35:08','/garbage/book.jpg','notebook',1,0.05);
/*!40000 ALTER TABLE `garbage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `garbage_sort_result`
--

DROP TABLE IF EXISTS `garbage_sort_result`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `garbage_sort_result` (
  `gid` int NOT NULL,
  `uid` int NOT NULL,
  `times` int NOT NULL DEFAULT '0',
  `correctTimes` int NOT NULL DEFAULT '0',
  `unlockTime` datetime DEFAULT NULL,
  PRIMARY KEY (`gid`,`uid`),
  KEY `uid` (`uid`),
  CONSTRAINT `garbage_sort_result_ibfk_1` FOREIGN KEY (`gid`) REFERENCES `garbage` (`gid`),
  CONSTRAINT `garbage_sort_result_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `garbage_sort_result_chk_1` CHECK ((`correctTimes` <= `times`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garbage_sort_result`
--

LOCK TABLES `garbage_sort_result` WRITE;
/*!40000 ALTER TABLE `garbage_sort_result` DISABLE KEYS */;
INSERT INTO `garbage_sort_result` VALUES (1,10,0,0,NULL),(1,11,0,0,NULL),(1,12,0,0,NULL),(1,13,0,0,NULL),(5,10,0,0,NULL),(5,11,0,0,NULL),(5,12,0,0,NULL),(5,13,0,0,NULL),(6,10,0,0,NULL),(6,11,0,0,NULL),(6,12,0,0,NULL),(6,13,0,0,NULL),(7,10,0,0,NULL),(7,11,0,0,NULL),(7,12,0,0,NULL),(7,13,0,0,NULL),(10,10,0,0,NULL),(10,11,0,0,NULL),(10,12,0,0,NULL),(10,13,0,0,NULL),(12,10,0,0,NULL),(12,11,0,0,NULL),(12,12,0,0,NULL),(12,13,0,0,NULL),(14,10,0,0,NULL),(14,11,1,1,'2021-06-25 15:41:45'),(14,12,0,0,NULL),(14,13,0,0,NULL),(18,10,0,0,NULL),(18,11,0,0,NULL),(18,12,0,0,NULL),(18,13,0,0,NULL),(21,10,0,0,NULL),(21,11,0,0,NULL),(21,12,0,0,NULL),(21,13,0,0,NULL),(23,10,0,0,NULL),(23,11,0,0,NULL),(23,12,0,0,NULL),(23,13,0,0,NULL);
/*!40000 ALTER TABLE `garbage_sort_result` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scene`
--

DROP TABLE IF EXISTS `scene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `scene` (
  `sid` int NOT NULL AUTO_INCREMENT,
  `minX` int NOT NULL,
  `maxX` int NOT NULL,
  `minY` int NOT NULL,
  `maxY` int NOT NULL,
  `minZ` int NOT NULL,
  `maxZ` int NOT NULL,
  `name` varchar(2048) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scene`
--

LOCK TABLES `scene` WRITE;
/*!40000 ALTER TABLE `scene` DISABLE KEYS */;
INSERT INTO `scene` VALUES (1,-54,54,0,0,-54,54,'默认场景');
/*!40000 ALTER TABLE `scene` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `username` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (10,'admin','$2a$10$hfY6X0TCyiVXhVSgSIS9Y.5OxQTfs7FT0Ad8N4xY/65vS1xpNs.L2'),(11,'Alice','$2a$10$CS6sAVTak2EocBy4TIB9Bu/5XUWNB9sBjAv29WO.VBDCl6l6c5D1u'),(12,'Bob','$2a$10$vJ0arYlTeNEEHRbsxrlgS.TSRN5wkeo9PQncsJOLUKDE3tYU2bum6'),(13,'Cathy','$2a$10$.yUh.CCknxYBV/J8GfGHwuUhczvB7p0YWHD4VL0au2AbXayqGN08C');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_appearance`
--

DROP TABLE IF EXISTS `user_appearance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_appearance` (
  `uid` int NOT NULL,
  `color` varchar(128) NOT NULL,
  `url` varchar(128) NOT NULL,
  PRIMARY KEY (`uid`),
  CONSTRAINT `user_appearance_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_appearance`
--

LOCK TABLES `user_appearance` WRITE;
/*!40000 ALTER TABLE `user_appearance` DISABLE KEYS */;
INSERT INTO `user_appearance` VALUES (10,'blue','models/au_blue'),(11,'orange','models/au_orange'),(12,'blue','models/au_blue'),(13,'blue','models/au_blue');
/*!40000 ALTER TABLE `user_appearance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_authority`
--

DROP TABLE IF EXISTS `user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_authority` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aid` int NOT NULL,
  `uid` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `aid` (`aid`),
  CONSTRAINT `user_authority_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`),
  CONSTRAINT `user_authority_ibfk_2` FOREIGN KEY (`aid`) REFERENCES `authority` (`aid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_authority`
--

LOCK TABLES `user_authority` WRITE;
/*!40000 ALTER TABLE `user_authority` DISABLE KEYS */;
INSERT INTO `user_authority` VALUES (11,1,10),(12,2,10),(13,1,11),(14,2,11),(15,1,12),(16,2,12),(17,1,13),(18,2,13);
/*!40000 ALTER TABLE `user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_login_info`
--

DROP TABLE IF EXISTS `user_login_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_login_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `token` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  CONSTRAINT `user_login_info_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_login_info`
--

LOCK TABLES `user_login_info` WRITE;
/*!40000 ALTER TABLE `user_login_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_login_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_score`
--

DROP TABLE IF EXISTS `user_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_score` (
  `uid` int NOT NULL,
  `score` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`),
  CONSTRAINT `user_score_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_score`
--

LOCK TABLES `user_score` WRITE;
/*!40000 ALTER TABLE `user_score` DISABLE KEYS */;
INSERT INTO `user_score` VALUES (10,0),(11,3),(12,0),(13,0);
/*!40000 ALTER TABLE `user_score` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-26  1:07:45
