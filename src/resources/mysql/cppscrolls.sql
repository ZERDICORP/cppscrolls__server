-- MariaDB dump 10.19  Distrib 10.7.3-MariaDB, for Linux (x86_64)
--
-- Host: localhost    Database: cppscrolls
-- ------------------------------------------------------
-- Server version	10.7.3-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `scroll_topic`
--

DROP TABLE IF EXISTS `scroll_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scroll_topic` (
  `scroll_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `topic_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`scroll_id`,`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scrolls`
--

DROP TABLE IF EXISTS `scrolls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scrolls` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `author_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `script_func` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `test_func` mediumtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `side` int(11) NOT NULL,
  `successful_attempts` int(11) NOT NULL DEFAULT 0,
  `unsuccessful_attempts` int(11) NOT NULL DEFAULT 0,
  `_order_marker` int(11) NOT NULL AUTO_INCREMENT,
  `bad_reputation` tinyint(1) NOT NULL DEFAULT 0,
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `_order_marker` (`_order_marker`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `topics`
--

DROP TABLE IF EXISTS `topics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `topics` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `requests` int(11) NOT NULL DEFAULT 0,
  `side` int(11) NOT NULL,
  PRIMARY KEY (`name`,`side`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unique_scroll_visits`
--

DROP TABLE IF EXISTS `unique_scroll_visits`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unique_scroll_visits` (
  `scroll_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bad_mark` tinyint(1) NOT NULL DEFAULT 0,
  `solution` mediumtext COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nickname` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(320) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password_hash` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `bio` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `image` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `score` int(11) NOT NULL DEFAULT 0,
  `side` int(11) NOT NULL,
  `confirmed` tinyint(1) NOT NULL DEFAULT 0,
  `scroll_creation_time` datetime DEFAULT NULL,
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `nickname` (`nickname`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `id_2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-04-09 22:42:22
