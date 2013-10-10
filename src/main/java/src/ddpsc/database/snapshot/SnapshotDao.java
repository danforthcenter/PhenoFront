package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.List;

/**
 * Required interface for interacting with the database. Implemented by the corresponding impl Class.
 * 
 * @author shill
 *
 */
public interface SnapshotDao {
	public Snapshot findBySnapshotId(int id);
	public Snapshot findWithTileBySnapshotId(int id);
	public List<Snapshot> findSnapshotAfterTimestamp(Timestamp timestamp);
	public List<Snapshot> findWithTileAfterTimestamp(Timestamp timestamp);
	public List<Snapshot> findSnapshotBetweenTimes(Timestamp before, Timestamp after);
	public List<Snapshot> findWithTileBetweenTimes(Timestamp before, Timestamp after);
}
