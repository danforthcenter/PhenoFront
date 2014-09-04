USE `PhenoFront`;

-- --------------------------------------------------------

--
-- Add list of downloaded snapshots to query metadata
--
CREATE TABLE IF NOT EXISTS `experiment_metadata` (
	`experiment_id`			INT(10) UNSIGNED NOT NULL,
	`experiment_name`		VARCHAR(45) NOT NULL,

	`number_snapshots`		INT(10) UNSIGNED NOT NULL,
	`number_tiles`			INT(10) UNSIGNED NOT NULL,
	`last_synchronized`		DATETIME,

	PRIMARY KEY(`experiment_id`),
	UNIQUE KEY(`experiment_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;