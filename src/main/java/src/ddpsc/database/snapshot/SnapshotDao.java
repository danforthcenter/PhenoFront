package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

/**
 * Required interface for interacting with the database. Implemented by the
 * corresponding impl Class.
 * 
 * @author shill
 * 
 */
public interface SnapshotDao {
	public Snapshot findBySnapshotId(int id);

	public Snapshot findWithTileBySnapshotId(int id);

	public List<Snapshot> findSnapshotAfterTimestamp(Timestamp timestamp);

	public List<Snapshot> findSnapshotAfterTimestampImageJobs(
			Timestamp timestamp);

	public List<Snapshot> findWithTileAfterTimestamp(Timestamp timestamp);

	public List<Snapshot> findSnapshotBetweenTimes(Timestamp before,
			Timestamp after);

	public List<Snapshot> findSnapshotBetweenTimesImageJobs(Timestamp before,
			Timestamp after);

	public List<Snapshot> findWithTileBetweenTimes(Timestamp before,
			Timestamp after);

	public List<Snapshot> findWithTileLastNEntries(int n);

	public List<Snapshot> findWithTileLastNEntriesImageJobs(int n);

	public List<Snapshot> findSnapshotLastNEntries(int n);

	public void setDataSource(DataSource dataSource);

	public List<Snapshot> findSnapshotCustomQueryImageJobs(Timestamp after,
			Timestamp before, String plantBarcode, String measurementLabel);
	public List<Snapshot> findWithTileCustomQueryImageJobs(Timestamp after,
			Timestamp before, String plantBarcode, String measurementLabel);
}
