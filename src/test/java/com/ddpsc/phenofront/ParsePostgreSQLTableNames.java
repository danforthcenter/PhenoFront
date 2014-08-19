package com.ddpsc.phenofront;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class ParsePostgreSQLTableNames implements RowMapper<String>
{
	@Override
	public String mapRow(ResultSet resultSet, int line) throws SQLException
	{
		return resultSet.getString("table_name");
	}
}