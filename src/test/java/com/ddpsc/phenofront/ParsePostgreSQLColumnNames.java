package com.ddpsc.phenofront;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class ParsePostgreSQLColumnNames implements RowMapper<String>
{
	@Override
	public String mapRow(ResultSet resultSet, int line) throws SQLException
	{
		return resultSet.getString("column_name");
	}
}