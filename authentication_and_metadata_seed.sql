-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 07, 2014 at 08:22 PM
-- Server version: 5.5.32
-- PHP Version: 5.4.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `PhenoFront`
--
CREATE DATABASE IF NOT EXISTS `PhenoFront` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `PhenoFront`;

-- --------------------------------------------------------

--
-- Table structure for table `queries`
--
CREATE TABLE IF NOT EXISTS `queries` (
	`query_id`				INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`metadata_id`			INT(10) UNSIGNED,

	`experiment`			VARCHAR(255),
	`barcode_regex`			VARCHAR(255),
	`measurement_regex`		VARCHAR(255),
	`start_time`			DATETIME,
	`end_time`				DATETIME,

	`include_watering`		BOOL NOT NULL,
	`include_visible`		BOOL NOT NULL,
	`include_fluorescent`	BOOL NOT NULL,
	`include_infrared`		BOOL NOT NULL,
	PRIMARY KEY (`query_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

--
-- Table structure for table `query_metadata`
--
CREATE TABLE IF NOT EXISTS `query_metadata` (
	`metadata_id`			INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`query_id`				INT(10) UNSIGNED NOT NULL,
	`user_id`				INT(10) UNSIGNED,
	
	`comment`				TEXT,
	
	`date_made`				DATETIME,
	`date_download_begin`	DATETIME,
	`date_download_complete`DATETIME,
	`interrupted`			BOOL,
	`missed_snapshots`		TEXT,

	`bytes`					BIGINT,
	`number_snapshots`		INT,
	`number_tiles`			INT,
	
	PRIMARY KEY (`metadata_id`),
	INDEX (`comment`(2)), 			-- To check for commented / not commented
	FOREIGN KEY (`query_id`) REFERENCES `queries` (`query_id`) ON DELETE CASCADE,
	FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE SET NULL
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;
	

--
-- Table structure for table `tags`
--
CREATE TABLE IF NOT EXISTS `tags` (
	`tag_id`   INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	`tag_name` VARCHAR(255),
	PRIMARY KEY (`tag_id`),
	KEY (`tag_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

--
-- Table structure for table `groups`
--
CREATE TABLE IF NOT EXISTS `groups` (
  `group_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(10) unsigned NOT NULL,
  `group_name` varchar(45) NOT NULL,
  PRIMARY KEY (`group_id`),
  UNIQUE KEY `group_name` (`group_name`),
  KEY `owner_id` (`owner_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

--
-- Dumping data for table `groups`
--

INSERT INTO `groups` (`group_id`, `owner_id`, `group_name`) VALUES
(1, 2, 'Bioinfo'),
(3, 2, 'empty');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `USER_ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(45) NOT NULL,
  `PASSWORD` varchar(256) NOT NULL,
  `ENABLED` tinyint(1) NOT NULL,
  `GROUP_ID` int(10) unsigned DEFAULT NULL,
  `AUTHORITY` varchar(25) NOT NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `USERNAME` (`USERNAME`),
  KEY `GROUP_ID` (`GROUP_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`USER_ID`, `USERNAME`, `PASSWORD`, `ENABLED`, `GROUP_ID`, `AUTHORITY`) VALUES
(2, 'admin', '6a6f5cd3015c185a2a76e9a7b3eb75136ffbe977155a2fedcd8400d6ca6b6268b4f9c4191ae27026', 1, 1, 'ROLE_ADMIN');

--
-- Constraints for table `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `FK_OWNER_ID` FOREIGN KEY (`owner_id`) REFERENCES `users` (`USER_ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK_GROUP_ID` FOREIGN KEY (`GROUP_ID`) REFERENCES `groups` (`group_id`) ON DELETE SET NULL ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
