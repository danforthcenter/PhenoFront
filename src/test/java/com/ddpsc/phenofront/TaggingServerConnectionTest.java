package com.ddpsc.phenofront;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import junitx.framework.ListAssert;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.config.Config;
import src.ddpsc.database.tagging.TaggingDaoImpl;
import src.ddpsc.exceptions.MalformedConfigException;

/**
 * 
 * Tests the tagging database, ensuring that it has the structure defined by:
 * 		Tables:	
 * 			tags
 * 
 * 		Table "tags":
 * 			tag_id			INT(10)	UNSIGNED NOT NULL PRIMARY KEY
 * 													AUTO_INCREMENT <-- Not tested for
 * 			tag_name		VARCHAR(255) KEY
 * 			
 * 
 * The names of the table and its column names are defined in {@link TaggingDaoImpl}
 * as static strings. This test methods references those static strings.
 * 
 * @see TaggingDaoImpl
 * 
 */
public class TaggingServerConnectionTest
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Test Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Test
	public void TaggingDatabaseConfigurationFileConfiguredCorrectly() throws IOException
	{
		try {
			DataSource taggingDataSource = Config.userDatabaseDataSource();
			assertNotNull(taggingDataSource);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void TaggingServerHasCorrectTables() throws IOException
	{
		try {
			DataSource taggingDataSource = Config.userDatabaseDataSource();
			
			String getTables = "SHOW TABLES";
			
			JdbcTemplate taggingDatabase = new JdbcTemplate(taggingDataSource);
			List<String> tableNames = taggingDatabase.query(getTables, new ParseMySQLTableNames());
			
			ListAssert.assertContains(tableNames, TaggingDaoImpl.TAG_TABLE);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void TagsTableHasCorrectColumns() throws IOException
	{
		try {
			DataSource taggingDataSource = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE " + TaggingDaoImpl.TAG_TABLE;
			
			JdbcTemplate taggingDatabase = new JdbcTemplate(taggingDataSource);
			List<MySQLColumn> columns = taggingDatabase.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.TAG_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.TAG_NAME,	MySQLColumn.VARCHAR(255),		MySQLColumn.CAN_NULL,	MySQLColumn.MULTIPLE_KEY));
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
}
