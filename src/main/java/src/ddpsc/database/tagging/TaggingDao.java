package src.ddpsc.database.tagging;

import java.util.List;

import javax.sql.DataSource;

import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.tile.Tile;

/**
 * Abstract interface for accessing metadata and tagging information assigned
 * to particular snapshots.
 * 
 * @author cjmcentee
 */
public interface TaggingDao
{
	void setMetadataDataSource(DataSource database);
	
	// Snapshots
	List<Integer> findSnapshotsWithTag(String experimentName, String tag);
	List<Integer> findSnapshotsWithTags(String experimentName, List<String> tags);
	
	void loadSnapshotsWithTags(List<Snapshot> snapshots);
	
	void changeSnapshotTag(int id, String experiment, String newTag);
	void changeSnapshotTags(List<Integer> snapshotIds, String experiment, String newTag);
	
	void removeSnapshotTags(List<Integer> snapshotIds, String experiment);
	
	// Tiles
	void loadTilesWithTags(List<Tile> tiles, String experiment);
	
	void changeTileTag(int id, String experiment, String newTag);
	void changeTileTags(List<Integer> tileIds, String experiment, String newTag);
	
	void removeTileTags(List<Integer> tileIds, String experiment);
}
