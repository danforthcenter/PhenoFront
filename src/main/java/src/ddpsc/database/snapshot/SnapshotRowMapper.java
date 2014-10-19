package src.ddpsc.database.snapshot;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * This class creates a mapping from the database to our Snapshot object's fields. The inner class SnapshotExtractor
 * implements a spring utility function for this purpose, rather than putting it in it's own file, our application
 * will implement extractors as inner methods.
 * 
 * 
 * @see Snapshot
 * @see Snapshot.SNAPSHOT_QUERY_VARIABLES
 * 
 * @author shill, cjmcentee
 *
 */
public class SnapshotRowMapper implements RowMapper<Snapshot>{

	private final String experiment;
	public SnapshotRowMapper(String experiment)
	{
		this.experiment = experiment;
	}
	
	@Override
	public Snapshot mapRow(ResultSet resultSet, int line) throws SQLException
	{
		Snapshot snapshot = new Snapshot(
				resultSet.getInt(SnapshotDaoImpl.SNAPSHOT_ID),
				experiment,
				
				resultSet.getString(SnapshotDaoImpl.BARCODE),
				resultSet.getString(SnapshotDaoImpl.MEASUREMENT_LABEL),
				resultSet.getString(SnapshotDaoImpl.CAR_TAG),
				resultSet.getTimestamp(SnapshotDaoImpl.TIMESTAMP),
				
				resultSet.getFloat(SnapshotDaoImpl.WEIGHT_BEFORE),
				resultSet.getFloat(SnapshotDaoImpl.WEIGHT_AFTER),
				resultSet.getFloat(SnapshotDaoImpl.WATER_AMOUNT),
				
				resultSet.getBoolean(SnapshotDaoImpl.COMPLETED) );
		
		return snapshot;
	}
} 