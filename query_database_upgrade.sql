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