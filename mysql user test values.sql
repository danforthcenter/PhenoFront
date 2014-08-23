USE PhenoFront;

SHOW TABLES;

DESCRIBE users;

DROP TABLE users;

SELECT * FROM users;

DESCRIBE queries;

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(6, "cjmcentee2", "6a6f5cd3015c185a2a76e9a7b3eb75136ffbe977155a2fedcd8400d6ca6b6268b4f9c4191ae27026", 1, 1, "ROLE_USER");

DELETE FROM users WHERE USER_ID = 3;

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(3, "doug", "6a6f5cd3015c185a2a76e9a7b3eb75136ffbe977155a2fedcd8400d6ca6b6268b4f9c4191ae27026", 1, 2, "ROLE_ADMIN");

DELETE FROM users WHERE USER_ID = 5;

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(5, "steven", "6a6f5cd3015c185a2a76e9a7b3eb75136ffbe977155a2fedcd8400d6ca6b6268b4f9c4191ae27026", 1, 1, "ROLE_ADMIN");

DELETE FROM users WHERE USER_ID = 4;

INSERT  INTO users(USER_ID, USERNAME, PASSWORD, ENABLED, GROUP_ID, AUTHORITY)
VALUES(4, "johnsmith", "6a6f5cd3015c185a2a76e9a7b3eb75136ffbe977155a2fedcd8400d6ca6b6268b4f9c4191ae27026", 1, 3, "ROLE_USER");


SELECT EXISTS(SELECT 1 FROM users WHERE user_id ='1' LIMIT 1);

DESCRIBE groups;

SELECT * FROM groups;

DESCRIBE queries;

INSERT INTO groups(group_id, owner_id, group_name)
VALUES(2, 2, "test group");

CREATE TABLE IF NOT EXISTS `lemnatest` (
	`snapshot_id` int(10) unsigned NOT NULL,
	PRIMARY KEY (`snapshot_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 ;

SHOW TABLES;

DELETE FROM lemnatest_to_tags WHERE snapshot_id = 3234322;

SELECT * FROM tags;

SELECT t.tag_name FROM tags t NATURAL JOIN LemnaTest s  NATURAL JOIN LemnaTest_to_tags rt  WHERE rt.snapshot_id = '28214';

SELECT lt.snapshot_id FROM lemnatest lt
NATURAL JOIN lemnatest_to_tags;

DROP TABLES lemnatest, lemnatest_to_tags, tags;

USE phenofront;

DESCRIBE groups;

DESCRIBE queries;

SHOW TABLES;

SELECT * FROM query_metadata;

SELECT * FROM tags NATURAL JOIN lemnatest_tiles;

SELECT * FROM users WHERE user_id = '3';

USE phenofront;

SELECT u.username AS owner, u.user_id AS owner_id, gr.group_name, gr.group_id
FROM groups AS gr JOIN users AS u ON u.user_id = gr.owner_id;

SELECT u.username AS owner, u.user_id AS owner_id, gr.group_name, gr.group_id
FROM groups AS gr JOIN users AS u ON u.user_id = gr.owner_id WHERE gr.group_name='Bioinfo';

SELECT * FROM users;

SELECT * FROM groups;

UPDATE users SET group_id='10' WHERE user_id='2';

SELECT * FROM users WHERE user_id='3' OR username='admin';

UPDATE users SET authority='ROLE_ADMIN' WHERE authority='admin';

DESCRIBE query_metadata;
DESCRIBE queries;
DESCRIBE tags;

INSERT INTO queries (
	experiment,
	barcode_regex,
	measurement_regex,
	start_time,
	end_time,
	include_watering,
	include_visible,
	include_infrared,
	include_fluorescent )
VALUES (
	'LemnaTest',
	'DBA',
	'bio',
	'2000-01-01 01:01:01',
	null,
	true,
	false,
	false,
	false );

DELETE FROM queries WHERE query_id = 3;

SELECT * FROM queries;

SELECT * FROM query_metadata WHERE comment = 'asdf';

SELECT * FROM query_metadata;

SELECT * FROM query_metadata WHERE comment IS NOT NULL;

INSERT INTO query_metadata (
	query_id,
	user_id,
	date_made,
	number_snapshots,
	number_tiles,
	has_comment)
VALUES (
	'1',
	'3',
	'2014-08-10 19:09:16.97',
	'15',
	'0',
	false );

DESCRIBE query_metadata;

SELECT q.experiment, m.date_made FROM queries AS q
JOIN query_metadata AS m ON q.query_id = m.query_id;

SELECT  
	q.query_id,
	q.experiment,
	q.barcode_regex,
	q.measurement_regex,
	q.start_time,
	q.end_time,
	q.include_watering,
	q.include_visible,
	q.include_fluorescent,
	q.include_infrared,
	m.metadata_id,
	m.user_id,
	m.has_comment,
	m.comment,
	m.date_made AS date,
	m.date_download_begin,
	m.date_download_complete,
	m.interrupted,
	m.bytes,
	m.number_snapshots,
	m.number_tiles,
	u.USERNAME
FROM queries AS q
JOIN query_metadata AS m ON q.query_id = m.query_id
JOIN users AS u ON u.USER_ID = m.user_id
ORDER BY date DESC
LIMIT 10;

UPDATE query_metadata SET interrupted = false WHERE query_id ='6' LIMIT 1;

SELECT * FROM queries AS q NATURAL JOIN query_metadata WHERE q.query_id = '6';