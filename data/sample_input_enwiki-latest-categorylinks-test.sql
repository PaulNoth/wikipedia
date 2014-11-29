-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: 10.64.32.21    Database: enwiki
-- ------------------------------------------------------
-- Server version       5.5.34-MariaDB-1~precise-log

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
-- Table structure for table `categorylinks`
--

DROP TABLE IF EXISTS `categorylinks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categorylinks` (
  `cl_from` int(8) unsigned NOT NULL DEFAULT '0',
  `cl_to` varbinary(255) NOT NULL DEFAULT '',
  `cl_sortkey` varbinary(230) NOT NULL DEFAULT '',
  `cl_timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cl_sortkey_prefix` varbinary(255) NOT NULL DEFAULT '',
  `cl_collation` varbinary(32) NOT NULL DEFAULT '',
  `cl_type` enum('page','subcat','file') NOT NULL DEFAULT 'page',
  UNIQUE KEY `cl_from` (`cl_from`,`cl_to`),
  KEY `cl_timestamp` (`cl_to`,`cl_timestamp`),
  KEY `cl_collation` (`cl_collation`),
  KEY `cl_sortkey` (`cl_to`,`cl_type`,`cl_sortkey`,`cl_from`)
) ENGINE=InnoDB DEFAULT CHARSET=binary;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categorylinks`
--

/*!40000 ALTER TABLE `categorylinks` DISABLE KEYS */;
INSERT INTO `categorylinks` VALUES (0,'','','2014-01-16 15:23:19','','','page'),(10,'Redirects_with_old_history','ACCESSIBLECOMPUTING','2010-08-26 22:38:36','','uppercase','page'),(10,'Unprintworthy_redirects','ACCESSIBLECOMPUTING','2010-08-26 22:38:36','','uppercase','page'),(12,'Anarchism',' \nANARCHISM','2010-09-03 21:55:38',' ','uppercase','page'),(12,'Anti-capitalism','ANARCHISM','2012-08-13 12:09:07','','uppercase','page'),(12,'Anti-fascism','ANARCHISM','2011-05-23 04:16:34','','uppercase','page'),(12,'Articles_containing_Ancient_Greek-language_text','ANARCHISM','2014-07-28 16:14:59','','uppercase','page'),(12,'Articles_containing_Spanish-language_text','ANARCHISM','2013-06-17 04:28:02','','uppercase','page'),(12,'Articles_with_DMOZ_links','ANARCHISM','2014-07-25 01:58:23','','uppercase','page'),(12,'Articles_with_French-language_external_links','ANARCHISM','2013-06-17 04:28:02','','uppercase','page'),(12,'Articles_with_inconsistent_citation_formats','ANARCHISM','2013-09-27 13:38:38','','uppercase','page'),(12,'Far-left_politics','ANARCHISM','2013-11-23 20:28:00','','uppercase','page'),(12,'Good_articles','ANARCHISM','2011-07-19 10:52:41','','uppercase','page'),(12,'Political_culture','ANARCHISM','2009-05-20 17:50:53','','uppercase','page'),(12,'Political_ideologies','ANARCHISM','2009-05-20 17:50:53','','uppercase','page'),(12,'Social_theories','ANARCHISM','2009-06-03 21:40:01','','uppercase','page'),(12,'Use_British_English_from_January_2014','ANARCHISM','2014-01-30 04:59:00','','uppercase','page'),(12,'Wikipedia_indefinitely_move-protected_pages','ANARCHISM\nANARCHISM','2013-02-08 05:50:46','Anarchism','uppercase','page'),(13,'Redirects_with_old_history','AFGHANISTANHISTORY','2007-04-19 22:12:13','','uppercase','page'),(13,'Unprintworthy_redirects','AFGHANISTANHISTORY','2006-09-08 04:15:52','','uppercase','page'),(14,'Redirects_with_old_history','AFGHANISTANGEOGRAPHY','2007-04-19 22:12:13','','uppercase','page'),(14,'Unprintworthy_redirects','AFGHANISTANGEOGRAPHY','2006-09-08 04:15:36','','uppercase','page'),(15,'Redirects_with_old_history','AFGHANISTANPEOPLE','2007-04-19 22:12:13','','uppercase','page'),(15,'Unprintworthy_redirects','AFGHANISTANPEOPLE','2006-09-08 04:15:11','','uppercase','page'),(18,'Redirects_with_old_history','AFGHANISTANCOMMUNICATIONS','2007-04-19 22:12:14','','uppercase','page'),(18,'Unprintworthy_redirects','AFGHANISTANCOMMUNICATIONS','2006-09-08 04:14:42','','uppercase','page'),(19,'Redirects_with_old_history','AFGHANISTANTRANSPORTATIONS','2007-04-19 22:12:14','','uppercase','page'),(19,'Unprintworthy_redirects','AFGHANISTANTRANSPORTATIONS','2006-09-08 04:14:07','','uppercase','page'),(20,'Redirects_with_old_history','AFGHANISTANMILITARY','2007-04-19 22:12:14','','uppercase','page'),(20,'Unprintworthy_redirects','AFGHANISTANMILITARY','2006-09-08 04:13:27','','uppercase','page'),(21,'Redirects_with_old_history','AFGHANISTANTRANSNATIONALISSUES','2007-04-19 22:12:14','','uppercase','page');
