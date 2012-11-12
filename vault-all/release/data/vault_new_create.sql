-- MySQL dump 10.13  Distrib 5.1.61, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: jbossportal
-- ------------------------------------------------------
-- Server version	5.1.61-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
--
-- Current Database: `jbossportal`
--
CREATE USER 'portal'@'localhost' IDENTIFIED BY 'portalpassword';
CREATE DATABASE `jbossportal` CHARACTER SET=utf8;
GRANT ALL PRIVILEGES ON jbossportal.* TO 'portal'@'localhost' IDENTIFIED BY 'portalpassword' WITH GRANT OPTION;

/*DROP DATABASE IF EXISTS  `jbossportal`;

COMMIT;*/

/*CREATE DATABASE*/ /*IF NOT EXISTS `jbossportal` *//*!40100 DEFAULT CHARACTER SET utf8 */;

USE `jbossportal`;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` smallint(6) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` mediumtext NOT NULL,
  `votesperuser` smallint(6) NOT NULL DEFAULT '0',
  `maxvotesperbug` smallint(6) NOT NULL DEFAULT '10000',
  `votestoconfirm` smallint(6) NOT NULL DEFAULT '0',
  `defaultmilestone` varchar(20) NOT NULL DEFAULT '---',
  `depends` tinyint(4) DEFAULT '0',
  `classification_id` smallint(6) NOT NULL DEFAULT '1',
  `isactive` tinyint(4) NOT NULL DEFAULT '1',
  `milestoneurl` tinytext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_comment_relationship`
--

DROP TABLE IF EXISTS `VA_comment_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_comment_relationship` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `historyid` bigint(20) NOT NULL,
  `requestid` bigint(20) NOT NULL,
  `replyid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=196 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_history`
--

DROP TABLE IF EXISTS `VA_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestid` bigint(20) NOT NULL,
  `requestname` varchar(255) NOT NULL,
  `summary` text,
  `detail` text NOT NULL,
  `owner` varchar(255) NOT NULL,
  `createdby` varchar(255) NOT NULL,
  `createdtime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `signedby` varchar(255) DEFAULT NULL,
  `signedtime` datetime DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `comment` text,
  `value` bigint(20) DEFAULT NULL,
  `editedby` varchar(255) DEFAULT NULL,
  `editedtime` datetime DEFAULT NULL,
  `versionid` bigint(20) NOT NULL,
  `is_public` int(11) DEFAULT NULL,
  `forward` varchar(255) DEFAULT NULL,
  `request_version` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_reply_comment`
--

DROP TABLE IF EXISTS `VA_reply_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_reply_comment` (
  `replyid` bigint(20) NOT NULL AUTO_INCREMENT,
  `baseid` bigint(20) DEFAULT '-1',
  `replycomment` text NOT NULL,
  `editedby` varchar(255) NOT NULL,
  `editedtime` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`replyid`)
) ENGINE=MyISAM AUTO_INCREMENT=196 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_request`
--

DROP TABLE IF EXISTS `VA_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_request` (
  `requestid` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestname` varchar(255) NOT NULL,
  `summary` text CHARACTER SET latin1,
  `detail` text NOT NULL,
  `owner` varchar(1024) NOT NULL,
  `createdby` varchar(255) CHARACTER SET latin1 NOT NULL,
  `createdtime` datetime NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `signedby` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `signedtime` datetime DEFAULT NULL,
  `status` varchar(255) CHARACTER SET latin1 NOT NULL,
  `comment` text CHARACTER SET latin1,
  `value` bigint(20) DEFAULT NULL,
  `editedby` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `editedtime` datetime DEFAULT NULL,
  `versionid` bigint(20) NOT NULL,
  `is_public` int(11) DEFAULT NULL,
  `forward` varchar(1024) DEFAULT NULL,
  `request_version` int(11) DEFAULT '1',
  `from` int(11) DEFAULT '0',
  PRIMARY KEY (`requestid`)
) ENGINE=MyISAM AUTO_INCREMENT=1138 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_request_history`
--

DROP TABLE IF EXISTS `VA_request_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_request_history` (
  `historyid` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestid` bigint(20) NOT NULL,
  `editedby` varchar(255) CHARACTER SET latin1 NOT NULL,
  `editedtime` datetime DEFAULT NULL,
  `status` varchar(255) CHARACTER SET latin1 NOT NULL,
  `comment` text CHARACTER SET latin1 NOT NULL,
  `useremail` varchar(255) CHARACTER SET latin1 NOT NULL,
  `request_version` int(11) DEFAULT '1',
  `is_history` bit(1) DEFAULT b'0',
  PRIMARY KEY (`historyid`)
) ENGINE=MyISAM AUTO_INCREMENT=5240 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_request_relationship`
--

DROP TABLE IF EXISTS `VA_request_relationship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_request_relationship` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestid` bigint(20) NOT NULL,
  `relationshipid` bigint(20) NOT NULL,
  `is_parent` bit(1) NOT NULL,
  `request_version` int(11) DEFAULT '1',
  `enable` bit(1) DEFAULT b'1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=970 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_requestmap`
--

DROP TABLE IF EXISTS `VA_requestmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_requestmap` (
  `mapid` bigint(20) NOT NULL AUTO_INCREMENT,
  `mapname` varchar(255) CHARACTER SET latin1 NOT NULL,
  `mapvalue` varchar(255) CHARACTER SET latin1 NOT NULL,
  `requestid` bigint(20) DEFAULT NULL,
  `request_version` int(11) DEFAULT '1',
  PRIMARY KEY (`mapid`),
  KEY `FK5733F6812E07100F` (`requestid`),
  KEY `FK5733F681FFA3EFE1` (`requestid`)
) ENGINE=MyISAM AUTO_INCREMENT=12486 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_requestmap_prop`
--

DROP TABLE IF EXISTS `VA_requestmap_prop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_requestmap_prop` (
  `mapid` bigint(20) NOT NULL,
  `VA_value` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `VA_name` varchar(255) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`mapid`,`VA_name`),
  KEY `FKDA664E61FFE2526E` (`mapid`),
  KEY `FKDA664E61DE84895C` (`mapid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_savequery`
--

DROP TABLE IF EXISTS `VA_savequery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_savequery` (
  `queryid` bigint(20) NOT NULL AUTO_INCREMENT,
  `queryname` varchar(255) CHARACTER SET latin1 NOT NULL,
  `searchname` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `createdby` varchar(255) CHARACTER SET latin1 NOT NULL,
  `createdtime` datetime NOT NULL,
  `versionid` bigint(20) DEFAULT NULL,
  `status` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `owner` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `searchcreator` varchar(255) CHARACTER SET latin1 DEFAULT NULL,
  `type` varchar(255) CHARACTER SET latin1 NOT NULL,
  `productid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`queryid`)
) ENGINE=MyISAM AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_sendemail_count`
--

DROP TABLE IF EXISTS `VA_sendemail_count`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_sendemail_count` (
  `sendid` bigint(20) NOT NULL AUTO_INCREMENT,
  `requestid` bigint(20) NOT NULL,
  `requesttime` datetime DEFAULT NULL,
  `count` bigint(20) NOT NULL,
  PRIMARY KEY (`sendid`)
) ENGINE=MyISAM AUTO_INCREMENT=1131 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `VA_user`
--

DROP TABLE IF EXISTS `VA_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `VA_user` (
  `userid` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `useremail` varchar(255) NOT NULL,
  `createdtime` datetime NOT NULL,
  `is_admin` int(11) DEFAULT '0',
  `login_count` int(11) DEFAULT '0',
  PRIMARY KEY (`userid`)
) ENGINE=MyISAM AUTO_INCREMENT=791 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `versions`
--

DROP TABLE IF EXISTS `versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `versions` (
  `value` varchar(64) NOT NULL,
  `product_id` smallint(6) NOT NULL,
  `id` mediumint(9) NOT NULL,
  `isactive` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2012-11-08 16:52:38
