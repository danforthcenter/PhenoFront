package com.ddpsc.phenofront;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import junitx.framework.ListAssert;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.config.Config;
import src.ddpsc.exceptions.MalformedConfigException;

public class LemnaTecServerConnectionTest
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Test Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Test
	public void LemnaTecDatabaseConfigurationFileConfiguredCorrectly() throws IOException
	{
		try {
			DataSource lemnaTecDatabase = Config.experimentDataSource("LTSystem");
			assertNotNull(lemnaTecDatabase);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	
	
	@Test
	public void LemnaTecDatabaseHasExperiments() throws IOException
	{
		try {
			List<String> databaseNames = GetDatabaseNames();
			
			assertTrue(databaseNames.size() > 0);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	
	@Test
	public void LemnaTecExperimentsFormattedAccordingToSpec() throws IOException
	{
		try {
			List<String> databaseNames = GetDatabaseNames();
			
			for (String databaseName : databaseNames) {
				List<String> tableNames = GetTableNames(databaseName);
				
				// The database has the required tables
				ListAssert.assertContains(tableNames, "snapshot");
				ListAssert.assertContains(tableNames, "tile");
				ListAssert.assertContains(tableNames, "tiled_image");
			}
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void LemnaTecExperimentTablesHaveRequiredFields() throws IOException
	{
		try {
			List<String> databaseNames = GetDatabaseNames();
			
			List<String> columnNames = null;
			for (String databaseName : databaseNames) {
				
				// The snapshot table has the required columns
				columnNames = GetColumnNames("snapshot", databaseName);
				ListAssert.assertContains(columnNames, "id");
				ListAssert.assertContains(columnNames, "id_tag");
				ListAssert.assertContains(columnNames, "car_tag");
				ListAssert.assertContains(columnNames, "measurement_label");
				ListAssert.assertContains(columnNames, "time_stamp");
				ListAssert.assertContains(columnNames, "weight_before");
				ListAssert.assertContains(columnNames, "weight_after");
				ListAssert.assertContains(columnNames, "water_amount");
				ListAssert.assertContains(columnNames, "completed");
				
				// The tile table has the required columns
				columnNames =  GetColumnNames("tile", databaseName);
				ListAssert.assertContains(columnNames, "raw_image_oid");
				ListAssert.assertContains(columnNames, "raw_null_image_oid");
				ListAssert.assertContains(columnNames, "dataformat");
				ListAssert.assertContains(columnNames, "width");
				ListAssert.assertContains(columnNames, "height");
				ListAssert.assertContains(columnNames, "rotate_flip_type");
				ListAssert.assertContains(columnNames, "frame");
				
				// The tiled_image table has the required columns
				columnNames =  GetColumnNames("tiled_image", databaseName);
				ListAssert.assertContains(columnNames, "camera_label");
			}
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Database Access Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private List<String> GetDatabaseNames() throws MalformedConfigException, IOException
	{
		DataSource lemnaTecDatabase = Config.experimentDataSource("LTSystem");
		
		String getDatabases = "SELECT name FROM ltdbs WHERE removed = FALSE";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(lemnaTecDatabase);
		List<String> databaseNames = jdbcTemplate.query(getDatabases, new ParseLemnaTecDatabaseNames());
		
		return databaseNames;
	}
	
	private List<String> GetTableNames(String databaseName) throws MalformedConfigException, IOException
	{
		DataSource experimentDatabase = Config.experimentDataSource(databaseName);
		
		String getTables = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentDatabase);
		List<String> tableNames = jdbcTemplate.query(getTables, new ParsePostgreSQLTableNames());
		
		return tableNames;
	}
	
	private List<String> GetColumnNames(String table, String databaseName) throws MalformedConfigException, IOException
	{
		DataSource experimentDatabase = Config.experimentDataSource(databaseName);
		
		String getColumns = "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = '" + table + "'";
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(experimentDatabase);
		List<String> columnNames = jdbcTemplate.query(getColumns, new ParsePostgreSQLColumnNames());
		
		return columnNames;
	}
	
}
