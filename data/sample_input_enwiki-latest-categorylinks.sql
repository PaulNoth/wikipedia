-- MySQL dump 10.13  Distrib 5.5.35, for debian-linux-gnu (x86_64)
--
-- Host: 10.64.32.21    Database: enwiki
-- ------------------------------------------------------
-- Server version	5.5.34-MariaDB-1~precise-log

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
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `cat_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `cat_title` varbinary(255) NOT NULL DEFAULT '',
  `cat_pages` int(11) NOT NULL DEFAULT '0',
  `cat_subcats` int(11) NOT NULL DEFAULT '0',
  `cat_files` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_title` (`cat_title`),
  KEY `cat_pages` (`cat_pages`)
) ENGINE=InnoDB AUTO_INCREMENT=215171621 DEFAULT CHARSET=binary;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Redirects_from_title_without_diacritics',0,0,0),(2,'Unprintworthy_redirects',1049347,15,0),(3,'Computer_storage_devices',87,10,0),(4,'American_Animation_articles',0,0,0),(5,'Start-Class_American_Animation_articles',0,0,0),(6,'Low-importance_American_Animation_articles',0,0,0),(7,'Unknown-importance_Animation_articles',25,21,0),(8,'Low-importance_Animation_articles',11738,21,0),(9,'Vietnam_stubs',335,7,0),(10,'Rivers_of_Vietnam',72,59,0),(11,'Quang_Binh_Province',13,4,0),(12,'All_articles_with_unsourced_statements',275002,0,0),(13,'Articles_with_unsourced_statements_since_January_2008',0,0,0),(14,'Wikipedia_articles_needing_clarification',98,97,0),(15,'Articles_needing_additional_references_from_January_2008',2343,0,0),(16,'Comedy',105,23,0),(17,'Sociolinguistics',216,28,0),(18,'Figures_of_speech',123,12,0),(19,'Double_entendres',0,0,0),(20,'NASCAR_teams',68,3,0),(21,'Muhammad_Ali',32,3,0),(22,'Politics_and_government_work_group_articles',108719,4,0),(23,'Wikipedia_requested_photographs_of_politicians_and_government-people',9116,2,0),(24,'Stub-Class_biography_(politics_and_government)_articles',64868,0,0)

