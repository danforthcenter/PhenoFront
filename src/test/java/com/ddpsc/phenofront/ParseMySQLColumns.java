package com.ddpsc.phenofront;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class ParseMySQLColumns implements RowMapper<MySQLColumn>
{
	@Override
	public MySQLColumn mapRow(ResultSet resultSet, int line) throws SQLException
	{
		MySQLColumn col = new MySQLColumn(
				resultSet.getString("Field"),
				resultSet.getString("Type"),
				resultSet.getBoolean("Null"),
				resultSet.getString("Key"));
		
		return col;
	}
}