package com.ddpsc.phenofront;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import junitx.framework.ListAssert;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.config.Config;
import src.ddpsc.exceptions.MalformedConfigException;

public class UserProfileServerConnectionTest
{
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Test Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Test
	public void UserProfileDatabaseConfigurationFileConfiguredCorrectly() throws IOException
	{
		try {
			DataSource dataSource = Config.userDatabaseDataSource();
			assertNotNull(dataSource);
		}
		
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void UserProfileServerHasCorrectTables() throws IOException
	{
		try {
			DataSource userProfileDatabase = Config.userDatabaseDataSource();
			
			String getTables = "SHOW TABLES";
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(userProfileDatabase);
			List<String> tableNames = jdbcTemplate.query(getTables, new ParseMySQLTableNames());
			
			ListAssert.assertContains(tableNames, "groups");
			ListAssert.assertContains(tableNames, "users");
		}
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Ignore
	@Test
	public void UsersTablesHasCorrectColumns() throws IOException
	{
		try {
			DataSource userProfileDatabase = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE users";
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(userProfileDatabase);
			List<MySQLColumn> columnNames = jdbcTemplate.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columnNames, "USER_ID");
			ListAssert.assertContains(columnNames, "USERNAME");
			ListAssert.assertContains(columnNames, "PASSWORD");
			ListAssert.assertContains(columnNames, "ENABLED");
			ListAssert.assertContains(columnNames, "GROUP_ID");
			ListAssert.assertContains(columnNames, "AUTHORITY");
		}
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Ignore
	@Test
	public void GroupsTablesHasCorrectColumns() throws IOException
	{
		try {
			DataSource userProfileDatabase = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE groups";
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(userProfileDatabase);
			List<MySQLColumn> columnNames = jdbcTemplate.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columnNames, "group_id");
			ListAssert.assertContains(columnNames, "owner_id");
			ListAssert.assertContains(columnNames, "group_name");
		}
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
}
