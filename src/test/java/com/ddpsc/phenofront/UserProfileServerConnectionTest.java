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
import src.ddpsc.database.tagging.TaggingDaoImpl;
import src.ddpsc.database.user.Group;
import src.ddpsc.database.user.User;
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
	
	@Test
	public void UsersTablesHasCorrectColumns() throws IOException
	{
		try {
			DataSource userProfileDatabase = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE users";
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(userProfileDatabase);
			List<MySQLColumn> columns = jdbcTemplate.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(User.USER_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(User.USERNAME,	MySQLColumn.VARCHAR(45),		MySQLColumn.NEVER_NULL,	MySQLColumn.UNIQUE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(User.PASSWORD,	MySQLColumn.VARCHAR(256),		MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(User.ENABLED,	MySQLColumn.TINYINT(1),			MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(User.GROUP_ID,	MySQLColumn.INT_UNSIGNED(10),			MySQLColumn.CAN_NULL,	MySQLColumn.MULTIPLE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(User.AUTHORITY,	MySQLColumn.VARCHAR(25),		MySQLColumn.NEVER_NULL,	MySQLColumn.NOT_KEY));
		}
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
	
	@Test
	public void GroupsTablesHasCorrectColumns() throws IOException
	{
		try {
			DataSource userProfileDatabase = Config.userDatabaseDataSource();
			
			String getColumns = "DESCRIBE groups";
			
			JdbcTemplate jdbcTemplate = new JdbcTemplate(userProfileDatabase);
			List<MySQLColumn> columns = jdbcTemplate.query(getColumns, new ParseMySQLColumns());
			
			ListAssert.assertContains(columns, new MySQLColumn(Group.GROUP_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.PRIMARY_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(Group.OWNER_ID,	MySQLColumn.INT_UNSIGNED(10),	MySQLColumn.NEVER_NULL,	MySQLColumn.MULTIPLE_KEY));
			ListAssert.assertContains(columns, new MySQLColumn(Group.GROUP_NAME,MySQLColumn.VARCHAR(45),		MySQLColumn.NEVER_NULL,	MySQLColumn.UNIQUE_KEY));
		}
		catch (MalformedConfigException e) {
			fail("Configuration file missing fields.");
		}
	}
}
