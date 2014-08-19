package com.ddpsc.phenofront;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class ParseLemnaTecDatabaseNames implements RowMapper<String>
{
	@Override
	public String mapRow(ResultSet resultSet, int line) throws SQLException
	{
		return resultSet.getString("name");
	}
}