package com.ddpsc.phenofront;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import junitx.framework.ListAssert;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.config.Config;
import src.ddpsc.database.queries.QueryDaoImpl;
import src.ddpsc.exceptions.MalformedConfigException;

/**
 * 
 * Tests the query database, ensuring that it has the structure defined by:
 * 		Tables:	
 * 			queries
 * 			query_metadata
 * 
 * 
 * 		Table `queries`:
 * 			`query_id`				INT(10) UNSIGNED NOT NULL PRIMARY KEY
 * 																AUTO_INCREMENT <-- Not tested for 
 *			`metadata_id`			INT(10) UNSIGNED NOT NULL 
 *			`experiment`			VARCHAR(255)
 *			`barcode_regex`			VARCHAR(255)
 *			`measurement_regex`		VARCHAR(255)
 *			`start_time`			DATETIME
 *			`end_time`				DATETIME
 *
 *			`include_watering`		BOOL NOT NULL
 *			`include_visible`		BOOL NOT NULL
 *			`include_fluorescent`	BOOL NOT NULL
 *			`include_infrared`		BOOL NOT NULL
 * 
 * 
 * 		Table `query_metadata`:
 *			`metadata_id`			INT(10) UNSIGNED NOT NULL PRIMARY KEY
 *																AUTO_INCREMENT <-- Not tested for
 *			`query_id`				INT(10) UNSIGNED NOT NULL KEY
 *			`user_id`				INT(10) UNSIGNED NOT NULL KEY
 *			
 *			`comment`				TEXT KEY
 *			
 *			`date_made`				DATETIME
 *			`date_download_begin`	DATETIME
 *			`date_download_complete`DATETIME
 *			`interrupted`			BOOL
 *		
 *			`bytes`					BIGINT(20)
 *			`number_snapshots`		INT(11)
 *			`number_tiles`			INT(11)
 * 
 */
public class QueryServerConnectionTest
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Test Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Test
	public void QueryDatabaseConfigurationFileConfiguredCorrectly() throws IOException
	{
		try {
			DataSource queryDataSource = Config.userDatabaseDataSource();
			assertNotNull(queryDataSource);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void QueryServerHasCorrectTables() throws IOException
	{
		try {
			DataSource queryDataSource = Config.userDatabaseDataSource();
			
			String getTables = "SHOW TABLES";
			
			JdbcTemplate queryDatabase = new JdbcTemplate(queryDataSource);
			List<String> tableNames = queryDatabase.query(getTables, new ParseMySQLTableNames());
			
			ListAssert.assertContains(tableNames, QueryDaoImpl.QUERY_TABLE);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void QueryTableHasCorrectColumns() throws IOException
	{
		try {
			DataSource queryDataSource = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE " + QueryDaoImpl.QUERY_TABLE;
			
			JdbcTemplate queryDatabase = new JdbcTemplate(queryDataSource);
			List<MySQLColumn> columns = queryDatabase.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.QUERY_ID,		MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.METADATA_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.EXPERIMENT,		MySQLColumn.VARCHAR(255),		MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.BARCODE,		MySQLColumn.VARCHAR(255),		MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.MEASUREMENT,	MySQLColumn.VARCHAR(255),		MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.START_TIME,		MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.END_TIME,		MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.WATERING,		MySQLColumn.TINYINT(1),			MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.VISIBLE,		MySQLColumn.TINYINT(1),			MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.FLUORESCENT,	MySQLColumn.TINYINT(1),			MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.INFRARED,		MySQLColumn.TINYINT(1),			MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void QueryMetadataTableHasCorrectColumns() throws IOException
	{
		try {
			DataSource queryDataSource = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE " + QueryDaoImpl.METADATA_TABLE;
			
			JdbcTemplate queryDatabase = new JdbcTemplate(queryDataSource);
			List<MySQLColumn> columns = queryDatabase.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.METADATA_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.QUERY_ID,		MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.MULTIPLE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.USER_ID,		MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.CAN_NULL,	MySQLColumn.MULTIPLE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.COMMENT,		MySQLColumn.TEXT,				MySQLColumn.CAN_NULL,	MySQLColumn.MULTIPLE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.DATE_MADE,		MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.DOWNLOAD_BEGIN,	MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.DOWNLOAD_END,	MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.INTERRUPTED,	MySQLColumn.TINYINT(1),			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.SIZE,			MySQLColumn.BIGINT(20),			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.NUM_SNAPSHOTS,	MySQLColumn.INT(11),			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(QueryDaoImpl.NUM_TILES,		MySQLColumn.INT(11),			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
}
