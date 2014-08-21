package src.ddpsc.database.tagging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import src.ddpsc.database.snapshot.Snapshot;

class SnapshotsTagLoader implements ResultSetExtractor<List<Snapshot>>
{
	List<Snapshot> snapshots;
	public SnapshotsTagLoader(List<Snapshot> snapshots)
	{
		this.snapshots = snapshots;
	}
	
	private int indexById(int id, List<Snapshot> snapshots)
	{
		for (int index = 0; index < snapshots.size(); index++) {
			Snapshot snapshot = snapshots.get(index);
			if (snapshot.id == id)
				return index;
		}
		
		return -1;
	}
	
	public List<Snapshot> extractData(ResultSet resultSet) throws SQLException, DataAccessException
	{
		// The query execute returns the following values in order
		//		tag_name
		//		snapshot_id
		
		// n^2, but who cares for now
		// TODO: SQL query return sorted by ID to make this log n
		while (resultSet.next()) {
			String tag = resultSet.getString(TaggingDaoImpl.TAG_NAME);
			int id = resultSet.getInt(TaggingDaoImpl.SNAPSHOT_ID);
			int idIndex = indexById(id, this.snapshots);
			
			if (idIndex != -1) {
				snapshots.get(idIndex).setTag(tag);
			}
		}
		
		return snapshots;
	}
}