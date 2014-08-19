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
		
		snapshot.setId(resultSet.getInt(SnapshotDaoImpl.SNAPSHOT_ID));
		snapshot.setPlantBarcode(resultSet.getString(SnapshotDaoImpl.ID_TAG));
		snapshot.setCarTag(resultSet.getString(SnapshotDaoImpl.CAR_TAG));
		snapshot.setMeasurementLabel(resultSet.getString(SnapshotDaoImpl.MEASUREMENT_LABEL));
		snapshot.setTimeStamp(resultSet.getTimestamp(SnapshotDaoImpl.TIMESTAMP));
		snapshot.setWeightBefore(resultSet.getFloat(SnapshotDaoImpl.WEIGHT_BEFORE));
		snapshot.setWeightAfter(resultSet.getFloat(SnapshotDaoImpl.WEIGHT_AFTER));
		snapshot.setWaterAmount(resultSet.getFloat(SnapshotDaoImpl.WATER_AMOUNT));
		snapshot.setCompleted(resultSet.getBoolean(SnapshotDaoImpl.COMPLETED));
		return snapshot;
	}
} 