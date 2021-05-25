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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `emoji`
--

LOCK TABLES `emoji` WRITE;
/*!40000 ALTER TABLE `emoji` DISABLE KEYS */;
INSERT INTO `emoji` VALUES (1,'1','/emoji/1.jpg');
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
  `name` varchar(256) NOT NULL,
  `type` varchar(256) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `updateTime` datetime NOT NULL,
  `url` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`gid`),
  CONSTRAINT `garbage_chk_1` CHECK ((`type` in (_utf8mb3'干垃圾',_utf8mb3'湿垃圾',_utf8mb3'有害垃圾',_utf8mb3'可回收物')))
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garbage`
--

LOCK TABLES `garbage` WRITE;
/*!40000 ALTER TABLE `garbage` DISABLE KEYS */;
INSERT INTO `garbage` VALUES (1,'餐巾纸','干垃圾','一张普通的餐巾纸。没什么特别的，不过很常见。','2021-05-20 17:45:00','/garbage/napkin.jpg'),(2,'头发','干垃圾','满地都是头发。只有我的头上没有。','2021-05-20 17:45:00','/garbage/hair.jpg'),(3,'口香糖','干垃圾','平淡无奇的口香糖。','2021-05-20 17:45:00','/garbage/chewing_gum.jpg'),(4,'外卖餐盒','干垃圾','再忙也要记得按时吃饭呀。','2021-05-20 17:45:00','/garbage/take-out_box.png'),(5,'椰子壳','干垃圾','左半边还是右半边？其实都无所谓啦。','2021-05-20 17:45:00','/garbage/coconut_shell.jpg'),(6,'口红','干垃圾','一支口红。是因为过期还是别的原因被丢掉的就不得而知了。','2021-05-20 17:45:00','/garbage/lipstick.jpg'),(7,'西瓜皮','湿垃圾','还好没有直接踩上去。','2021-05-20 17:45:00','/garbage/watermelon_peel.jpg'),(8,'鸡蛋壳','湿垃圾','鸡蛋的壳。','2021-05-20 17:45:00','/garbage/egg_shell.jpg'),(9,'过期的薯片','湿垃圾','夜宵时间到了。等等，那包薯片过期了。','2021-05-20 17:45:00','/garbage/chips.jpg'),(10,'未吃完的饭菜','湿垃圾','好吃?不好吃。','2021-05-20 17:45:00','/garbage/leftovers.jpg'),(11,'花','湿垃圾','一束花。它盛开的样子很美呢。','2021-05-20 17:45:00','/garbage/flower.jpg'),(12,'废电池','有害垃圾','一块废旧电池。','2021-05-20 17:45:00','/garbage/battery.jpg'),(13,'过期药物','有害垃圾','过期啦！过期啦！过期啦！重要的事情说三遍。','2021-05-20 17:45:00','/garbage/medicine.jpg'),(14,'水银温度计','有害垃圾','测体温用的水银温度计。','2021-05-20 17:45:00','/garbage/clinical_thermometer.jpg'),(15,'空药瓶','有害垃圾','空空的药瓶。空空是谁？','2021-05-20 17:45:00','/garbage/medicine_bottle.jpg'),(16,'矿泉水瓶','可回收物','没有水的矿泉水瓶。','2021-05-20 17:45:00','/garbage/water_bottle.jpg'),(17,'牛奶盒','可回收物','一个牛奶盒，牛奶已经都被喝掉啦。','2021-05-20 17:45:00','/garbage/milk_bottle.jpg'),(18,'易拉罐','可回收物','看上去很有质感，不知道味道怎么样。','2021-05-20 17:45:00','/garbage/can.jpg'),(19,'装快递的纸板箱','可回收物','一个快递盒！是寄给谁的礼物呢？','2021-05-20 17:45:00','/garbage/express_box.jpg'),(20,'操作系统的课本','可回收物','别扔掉啊喂！','2021-05-20 17:45:00','/garbage/book.jpg'),(21,'碎玻璃','可回收物','玻璃水杯的碎片。记得先包起来以免划伤哦。','2021-05-20 17:45:00','/garbage/glass.jpg'),(22,'毛绒玩具','可回收物','很可爱的毛绒玩具。不要的话可以送给我。','2021-05-20 17:45:00','/garbage/stuffed_toy.jpg');
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
  CONSTRAINT `garbage_sort_result_chk_1` CHECK ((`correctTimes` <= `times`)),
  CONSTRAINT `garbage_sort_result_chk_2` CHECK ((((`unlockTime` is not null) and (`times` > 0)) or ((`unlockTime` is null) and (`times` = 0))))
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `garbage_sort_result`
--

LOCK TABLES `garbage_sort_result` WRITE;
/*!40000 ALTER TABLE `garbage_sort_result` DISABLE KEYS */;
INSERT INTO `garbage_sort_result` VALUES (1,1,0,0,NULL),(1,2,0,0,NULL),(1,3,0,0,NULL),(1,5,0,0,NULL),(2,1,0,0,NULL),(2,2,0,0,NULL),(2,3,0,0,NULL),(2,5,0,0,NULL),(3,1,0,0,NULL),(3,2,0,0,NULL),(3,3,0,0,NULL),(3,5,0,0,NULL),(4,1,0,0,NULL),(4,2,0,0,NULL),(4,3,0,0,NULL),(4,5,0,0,NULL),(5,1,0,0,NULL),(5,2,0,0,NULL),(5,3,0,0,NULL),(5,5,0,0,NULL),(6,1,0,0,NULL),(6,2,0,0,NULL),(6,3,0,0,NULL),(6,5,0,0,NULL),(7,1,0,0,NULL),(7,2,0,0,NULL),(7,3,0,0,NULL),(7,5,0,0,NULL),(8,1,0,0,NULL),(8,2,0,0,NULL),(8,3,0,0,NULL),(8,5,0,0,NULL),(9,1,0,0,NULL),(9,2,0,0,NULL),(9,3,0,0,NULL),(9,5,0,0,NULL),(10,1,0,0,NULL),(10,2,0,0,NULL),(10,3,0,0,NULL),(10,5,0,0,NULL),(11,1,0,0,NULL),(11,2,0,0,NULL),(11,3,0,0,NULL),(11,5,0,0,NULL),(12,1,0,0,NULL),(12,2,0,0,NULL),(12,3,0,0,NULL),(12,5,0,0,NULL),(13,1,0,0,NULL),(13,2,0,0,NULL),(13,3,0,0,NULL),(13,5,0,0,NULL),(14,1,0,0,NULL),(14,2,0,0,NULL),(14,3,0,0,NULL),(14,5,0,0,NULL),(15,1,0,0,NULL),(15,2,0,0,NULL),(15,3,0,0,NULL),(15,5,0,0,NULL),(16,1,0,0,NULL),(16,2,0,0,NULL),(16,3,0,0,NULL),(16,5,0,0,NULL),(17,1,0,0,NULL),(17,2,0,0,NULL),(17,3,0,0,NULL),(17,5,0,0,NULL),(18,1,0,0,NULL),(18,2,0,0,NULL),(18,3,0,0,NULL),(18,5,0,0,NULL),(19,1,0,0,NULL),(19,2,0,0,NULL),(19,3,0,0,NULL),(19,5,0,0,NULL),(20,1,0,0,NULL),(20,2,0,0,NULL),(20,3,0,0,NULL),(20,5,0,0,NULL),(21,1,0,0,NULL),(21,2,0,0,NULL),(21,3,0,0,NULL),(21,5,0,0,NULL),(22,1,0,0,NULL),(22,2,0,0,NULL),(22,3,0,0,NULL),(22,5,0,0,NULL);
/*!40000 ALTER TABLE `garbage_sort_result` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Admin','$2a$10$NM7s19sWIN4LBfBWHqkIKuaAk15j4kZndX5mqtDONmdxWseECQoxW'),(2,'Alice','$2a$10$pKMujZhdPtNDKwnY8I.RjOj1N2eGe9KiNv2a/OBSv65tGxH0TdjDe'),(3,'Bob','$2a$10$.e6FOBKAtWygZrGdPlrJiOHfAZH/F68C4rPH714aQRo.bF.s2uylq'),(5,'Test','$2a$10$gCbghuuAjsd.EUpIWFCNTOYgALSL4arbYpbdqkFTG2WFE4Ca4uHo2');
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
  PRIMARY KEY (`uid`),
  CONSTRAINT `user_appearance_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_appearance`
--

LOCK TABLES `user_appearance` WRITE;
/*!40000 ALTER TABLE `user_appearance` DISABLE KEYS */;
INSERT INTO `user_appearance` VALUES (1,'#FFFFFF'),(2,'#FFFFFF'),(3,'#FFFFFF'),(5,'#FFFFFF');
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_authority`
--

LOCK TABLES `user_authority` WRITE;
/*!40000 ALTER TABLE `user_authority` DISABLE KEYS */;
INSERT INTO `user_authority` VALUES (1,1,1),(2,2,1),(3,1,2),(4,1,3),(6,1,5);
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
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;
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
INSERT INTO `user_score` VALUES (1,0),(2,0),(3,0),(5,0);
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

-- Dump completed on 2021-05-25 23:46:31
