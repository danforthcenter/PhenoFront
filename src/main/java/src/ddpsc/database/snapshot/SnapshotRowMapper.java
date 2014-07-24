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

	@Override
	public Snapshot mapRow(ResultSet resultSet, int line) throws SQLException
	{
		// The order of the columns in the database is:
		//			id
		//			propagated
		//			configurationid
		//			idtag
		//			colour
		//			visited
		//			creator
		//			comment
		//			cartag
		//			measurementlabel
		//			timestanp
		//			weightbefore
		//			weightafter
		//			wateramount
		//			completed
		
		Snapshot snapshot = new Snapshot();
		
		snapshot.setID(resultSet.getInt(1)); // Should be 0, TODO: Figure out why not 0
		snapshot.setPlantBarcode(resultSet.getString(4));
		snapshot.setCarTag(resultSet.getString(9));
		snapshot.setMeasurementLabel(resultSet.getString(10));
		snapshot.setTimeStamp(resultSet.getTimestamp(11));
		snapshot.setWeightBefore(resultSet.getFloat(12));
		snapshot.setWeightAfter(resultSet.getFloat(13));
		snapshot.setWaterAmount(resultSet.getFloat(14));
		snapshot.setCompleted(resultSet.getBoolean(15));
		return snapshot;
	}
} 