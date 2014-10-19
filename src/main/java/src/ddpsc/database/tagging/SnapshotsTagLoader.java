package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import src.ddpsc.database.snapshot.Snapshot;

class SnapshotsTagLoader implements ResultSetExtractor<Boolean>
{
	Map<Integer, Snapshot> snapshots;
	public SnapshotsTagLoader(List<Snapshot> snapshots)
	{
		this.snapshots = Snapshot.getSnapshotIdMap(snapshots);
	}
	
	public Boolean extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		// The query execute returns the following values in order
		//		tag_name
		//		snapshot_id
		while (resultSet.next()) {
			String tag = resultSet.getString(TaggingDaoImpl.TAG_NAME);
			int id = resultSet.getInt(TaggingDaoImpl.SNAPSHOT_ID);
			snapshots.get(id).setTag(tag);
		}
		
		return resultSet.first();
	}
}