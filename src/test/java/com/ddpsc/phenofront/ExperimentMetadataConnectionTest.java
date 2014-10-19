package com.ddpsc.phenofront;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
 * Ensures the that following MySQL table exists in the appropriate location (specified by the user profile database config file),
 * and has the outlined structure
 * 
 *	Table `experiment_metadata`:
 * 			`experiment_id`			INT(10) UNSIGNED NOT NULL
 * 			`experiment_name`		VARCHAR(45) NOT NULL
 * 
 * 			`number_snapshots`		INT UNSIGNED NOT NULL
 * 			`number_tiles`			INT UNSIGNED NOT NULL
 * 			`last_synchronized`		DATETIME
 * 
 * @author cjmcentee
 *
 */
public class ExperimentMetadataConnectionTest
{
	@Test
	public void ExperimentMetadataDatabaseConfigurationFileConfiguredCorrectly() throws IOException
	{
		try {
			DataSource experimentMetadataDataSource = Config.userDatabaseDataSource();
			assertNotNull(experimentMetadataDataSource);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void ExperimentMetadataServerHasCorrectTables() throws IOException
	{
		try {
			DataSource experimentMetadataDataSource = Config.userDatabaseDataSource();
			
			String getTables = "SHOW TABLES";
			
			JdbcTemplate metadataDatabase = new JdbcTemplate(experimentMetadataDataSource);
			List<String> tableNames = metadataDatabase.query(getTables, new ParseMySQLTableNames());
			
			ListAssert.assertContains(tableNames, TaggingDaoImpl.EXPERIMENT_TABLE);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void ExperimentMetadataTableHasCorrectColumns() throws IOException
	{
		try {
			DataSource experimentMetadataDataSource = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE " + TaggingDaoImpl.EXPERIMENT_TABLE;
			
			JdbcTemplate metadataDatabase = new JdbcTemplate(experimentMetadataDataSource);
			List<MySQLColumn> columns = metadataDatabase.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.EXPERIMENT_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.EXPERIMENT_NAME,	MySQLColumn.VARCHAR(45),		MySQLColumn.NEVER_NULL,	MySQLColumn.UNIQUE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.NUMBER_SNAPSHOTS,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.NUMBER_TILES,		MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(TaggingDaoImpl.LAST_UPDATED,		MySQLColumn.DATETIME,			MySQLColumn.CAN_NULL,	MySQLColumn.NOT_KEY));
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
}
