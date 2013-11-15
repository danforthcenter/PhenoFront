package src.ddpsc.database.snapshot;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * This class creates a mapping from the database to our Snapshot object's fields. The inner class SnapshotExtractor
 * implements a spring utility function for this purpose, rather than putting it in it's own file, our application
 * will implement extractors as inner methods.
 * 
 * @throws SQLException
 * @see Snapshot
 * @author shill
 *
 */
public class SnapshotRowMapper implements RowMapper<Snapshot>{

	@Override
	public Snapshot mapRow(ResultSet resultSet, int line) throws SQLException {
		try{
			SnapshotExtractor userExtractor = new SnapshotExtractor();
			return userExtractor.extractData(resultSet);
		}catch (SQLException e){
			throw e;
		}
	}
}
/**
 * Inner class responsible for mapping table columns to fields in our database.
 * If a new field is added, this must be updated.
 * 
 * @author shill
 *
 */
class SnapshotExtractor implements ResultSetExtractor<Snapshot> {  
	public Snapshot extractData(ResultSet resultSet) throws SQLException,
			DataAccessException {
		Snapshot snapshot = new Snapshot();
		// id propagated configurationid idtag colour visited creator
		// comment cartag measurementlabel timestanp weightbefore weightafter
		// wateramount completed
		snapshot.setId(resultSet.getInt(1)); // why not 0 wtf?
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