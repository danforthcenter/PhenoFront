package src.ddpsc.database.tagging;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import src.ddpsc.database.experiment.Experiment;
import src.ddpsc.database.experiment.ExperimentRowMapper;
import src.ddpsc.database.snapshot.Snapshot;
import src.ddpsc.database.tile.Tile;
import src.ddpsc.utility.StringOps;

/**
 * Implementation of the metadata and tagging retrieval interface.
 * 
 * Retrieves data from a SQL database, defined by {@link metadataDataSource}.
 * 
 * Assumes the database has the structure of:
 * 		Tables:	
 * 			tags						describes each user defined tag
 * 			<experimentName>_snapshots	holds any user tagged snapshots from the LemnaTec system
 * 			<experimentName>_tiles		holds any user tagged tiles from the LemnaTec system
 * 			`experiment_metadata`		data describing each experiment
 * 
 * (where <experimentName> is the database name of the LemnaTec database)
 * 
 * 		Table "tags":
 * 			tag_name		VARCHAR		the tag
 * 			tag_id			INT			a unique ID
 * 
 * 		Table <experimentName>_snapshots
 * 			snapshot_id		INT			a unique ID, matches ID of a snapshot in the LemnaTec database
 * 			tag_id			INT			an ID, referencing a tag
 * 
 * 		Table <experimentName>_tiles
 * 			tile_id			INT			a unique ID, matches ID of a tile in the LemnaTec database
 * 			tag_id			INT			an ID, referencing a tag
 * 
 * 		Table `experiment_metadata`:
 * 			`experiment_id`			INT(10) UNSIGNED NOT NULL,	unique id
 * 			`experiment_name`		VARCHAR(45) NOT NULL,		the experiment name
 * 
 * 			`number_snapshots`		INT UNSIGNED NOT NULL,		number of snapshots since the last check
 * 			`number_tiles`			INT UNSIGNED NOT NULL,		number of tiles since the last check
 * 			`last_synchronized`		DATETIME,					last time the service checked the lemnatec database
 * 
 * @author cjmcentee
 */
public class TaggingDaoImpl implements TaggingDao
{
	private static final Logger log = Logger.getLogger(TaggingDaoImpl.class);
	
	public static final int MAX_TAGS_PER_QUERY = 10000;
	
	private DataSource metadataDataSource;
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// MySQL Table Description
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// The tag table
	public final static String TAG_TABLE = "tags";			// Table name
	public final static String TAG_NAME = "tag_name";		// Tag name variable name
	public final static String TAG_ID = "tag_id";			// Tag ID variable name
															// Has tag ID variable, found in the relation table below
	
	// The snapshots table
	private String snapshotTable(String experiment) { return experiment + SNAPSHOT_TABLE_SUFFIX; }
	public final static String SNAPSHOT_TABLE_SUFFIX = "_snapshots";	// Table name suffix
	public final static String SNAPSHOT_ID = "snapshot_id";				// Snapshot ID variable name
	
	// The tiles table
	private String tileTable(String experiment) { return experiment + TILE_TABLE_SUFFIX; }
	public final static String TILE_TABLE_SUFFIX = "_tiles";	// Table name suffix
	public final static String TILE_ID = "tile_id";				// Tile ID variable name
	
	// The experiment table
	public final static String EXPERIMENT_TABLE = "experiment_metadata";
	
	public final static String EXPERIMENT_ID = "experiment_id";
	public final static String EXPERIMENT_NAME = "experiment_name";
	public final static String NUMBER_SNAPSHOTS = "number_snapshots";
	public final static String NUMBER_TILES = "number_tiles";
	public final static String LAST_UPDATED = "last_synchronized";
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Runtime Table Modification
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private String addSnapshotTable(String experiment)
	{
		String snapshotTable = snapshotTable(experiment);
		
		String makeTable = "CREATE TABLE IF NOT EXISTS `" + snapshotTable + "` ("
				+ " `" + SNAPSHOT_ID + "` INT(10) UNSIGNED NOT NULL, "
				+ " `" + TAG_ID + "` INT(10) UNSIGNED NOT NULL, "
				+ " PRIMARY KEY (`" + SNAPSHOT_ID + "`), "
				+ " FOREIGN KEY (`" + TAG_ID + "`) REFERENCES `" + TAG_TABLE + "` (`" + TAG_ID + "`) ON DELETE CASCADE "
				+ " ) ENGINE=InnoDB  DEFAULT CHARSET=utf8";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(makeTable);
		
		return snapshotTable;
	}
	
	private String addTileTable(String experiment)
	{
		String tileTable = tileTable(experiment);
		
		String makeTable = "CREATE TABLE IF NOT EXISTS `" + tileTable + "` ("
				+ " `" + TILE_ID	+ "` INT(10) unsigned NOT NULL,"
				+ " `" + TAG_ID + "` INT(10) UNSIGNED NOT NULL, "
				+ " PRIMARY KEY (`" + TILE_ID + "`), "
				+ " FOREIGN KEY (`" + TAG_ID + "`) REFERENCES `" + TAG_TABLE + "` (`" + TAG_ID + "`) ON DELETE CASCADE "
				+ " ) ENGINE=InnoDB  DEFAULT CHARSET=utf8";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(makeTable);
		
		return tileTable;
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Snapshot Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	/**
	 * Returns any snapshots in the experiment that have any of the supplied tags
	 * 
	 * Can return a list with zero elements
	 * 
	 * @param	experiment		The name of the experiment to look for snapshots in
	 * @param	tag					A set of tags to match against the snapshots in the experiment
	 * @return						All snapshots that have at least one of the supplied tags
	 */
	@Override
	public List<Integer> findSnapshotsWithTags(String experiment, List<String> tags)
	{
		log.info("Attempting to retrieve snapshots from " + experiment + " with tags being one of " + tags + ".");
		
		String snapshotTable = addSnapshotTable(experiment);
		List<Integer> tagIds = addTags(tags);
		
		String getSnapshotsQuery = "SELECT s."+SNAPSHOT_ID + " "
				+ " FROM " + snapshotTable + " AS s "
				+ " NATURAL JOIN " + TAG_TABLE + " AS t "
				+ " WHERE t."+TAG_NAME + " IN (" + StringOps.idsAsCSV(tagIds) + ")";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		List<Integer> snapshotIds = taggingDatabase.query(getSnapshotsQuery, new SnapshotIdRowMapper());
		
		log.info(snapshotIds.size() + " many snapshots found to have tags being one of " + tags + "'.");
		
		return snapshotIds;
	}
	
	@Override
	public List<Integer> findSnapshotsWithTag(String experiment, String tag)
	{
		return findSnapshotsWithTags(experiment, Arrays.asList(new String[] {tag}));
	}
	
	@Override
	public void loadSnapshotsWithTags(List<Snapshot> snapshots)
	{
		log.info("Attempting to load " + snapshots.size() + "-many snapshots with tags.");
		
		log.info("i ranging up to " + snapshots.size() / MAX_TAGS_PER_QUERY);
		for (int i = 0; i < snapshots.size() / MAX_TAGS_PER_QUERY; i++) {
			log.info("i from " + i*MAX_TAGS_PER_QUERY + " to " + Math.min((i + 1)*MAX_TAGS_PER_QUERY, snapshots.size()));
			List<Snapshot> snapshotsSubList = snapshots.subList(
					i*MAX_TAGS_PER_QUERY,
					Math.min((i + 1)*MAX_TAGS_PER_QUERY, snapshots.size()));
			loadSnapshotsWithTags_HELPER(snapshotsSubList);
		}
		
		log.info(snapshots.size() + "-many snapshots have been loaded with tags.");
	}
	
	private void loadSnapshotsWithTags_HELPER(List<Snapshot> snapshots)
	{
		if (snapshots.size() == 0)
			return;
		
		String experiment = snapshots.get(0).experiment;
		String snapshotTable = addSnapshotTable(experiment);
		
		List<Integer> snapshotIds = Snapshot.getIds(snapshots);
		
		String tagQuery = "SELECT tag."+TAG_NAME + ", snapshot."+SNAPSHOT_ID + " "
				+ " FROM " + TAG_TABLE + " AS tag "
				+ " NATURAL JOIN " + snapshotTable + " AS snapshot "
				+ " WHERE snapshot."+SNAPSHOT_ID + " IN (" + StringOps.idsAsCSV(snapshotIds) + ")";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.query(tagQuery, new SnapshotsTagLoader(snapshots));
	}
	
	@Override
	public void changeSnapshotTags(List<Integer> snapshotIds, String experiment, String newTag)
	{
		if (snapshotIds.size() == 0) {
			log.warn("Instructed to set new tag, '" + newTag + "', on zero snapshots. Returning with logged warning.");
			return;
		}
		
		if (newTag == null || newTag == "") {
			removeSnapshotTags(snapshotIds, experiment);
			return;
		}
		
		
		log.info("Attempting to set new tag, '" + newTag + "', on the " + snapshotIds.size() + "-many snapshots.");
		
		String snapshotTable = addSnapshotTable(experiment);
		int tagId = addTag(newTag);
		
		String addTagRelation = "REPLACE INTO " + snapshotTable + " (" + TAG_ID + ", " + SNAPSHOT_ID + ") VALUES ";
		for (int i = 0; i < snapshotIds.size(); i++) {
			int id = snapshotIds.get(i);
			if (i != 0)
				addTagRelation += ", ";
			addTagRelation += "('" + tagId + "', '" + id + "')";
		}
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(addTagRelation);
		
		log.info("Successfully set the new tag, '" + newTag + "', on the " + snapshotIds.size() + "-many snapshots.");
	}
	
	@Override
	public void changeSnapshotTag(int id, String experiment, String newTag)
	{
		changeSnapshotTags(Arrays.asList(new Integer[] {id}), experiment, newTag);
	}
	
	@Override
	public void removeSnapshotTags(List<Integer> snapshotIds, String experiment)
	{
		log.info("Attempting to remove tags from " + snapshotIds.size() + "-many snapshots.");
		
		String snapshotTable = addSnapshotTable(experiment);
		
		String removeSnapshot = "DELETE FROM " + snapshotTable + " "
				+ " WHERE " + SNAPSHOT_ID + " IN (" + StringOps.idsAsCSV(snapshotIds) + ")";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(removeSnapshot);
		
		log.info("Successfully removed tags from " + snapshotIds.size() + "-many snapshots.");
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Tile Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public void loadTilesWithTags(List<Tile> tiles, String experiment)
	{
		if (tiles.size() == 0)
			return;
		
		log.info("Attempting to load " + tiles.size() + "-many tiles with tags.");
		
		log.info("tile tags index ranging up to " + tiles.size() / MAX_TAGS_PER_QUERY);
		for (int i = 0; i < tiles.size() / MAX_TAGS_PER_QUERY; i++) {
			
			log.info("tile tags index from " + i*MAX_TAGS_PER_QUERY + " to " + Math.min((i + 1)*MAX_TAGS_PER_QUERY, tiles.size()));
			
			List<Tile> tilesSubList = tiles.subList(
					i*MAX_TAGS_PER_QUERY,
					Math.min((i + 1)*MAX_TAGS_PER_QUERY, tiles.size()));
			loadTilessWithTags_HELPER(tilesSubList, experiment);
		}
		
		log.info(tiles.size() + "-many tiles have been loaded with tags.");
	}
	
	private void loadTilessWithTags_HELPER(List<Tile> tiles, String experiment)
	{
		String tileTable = addTileTable(experiment);
		List<Integer> tileIds = Tile.getIds(tiles);
		
		String tagQuery = "SELECT tag."+TAG_NAME + ", tile."+TILE_ID + " "
				+ " FROM " + TAG_TABLE + " AS tag "
				+ " NATURAL JOIN " + tileTable + " AS tile "
				+ " WHERE tile."+TILE_ID + " IN (" + StringOps.idsAsCSV(tileIds) + ") ";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.query(tagQuery, new TilesTagLoader(tiles));
	}
	
	@Override
	public void changeTileTags(List<Integer> tileIds, String experiment, String newTag)
	{
		if (tileIds.size() == 0) {
			log.warn("Instructed to set new tag, '" + newTag + "', on zero tiles. Returning with logged warning.");
			return;
		}
		
		if (newTag == null || newTag == "") {
			removeTileTags(tileIds, experiment);
			return;
		}
		
		
		log.info("Attempting to set new tag, '" + newTag + "', on " + tileIds.size() + "-many tiles.");
		
		String tileTable = addTileTable(experiment);
		int tagId = addTag(newTag);
		
		String addTagRelation = "REPLACE INTO " + tileTable + " (" + TAG_ID + ", " + TILE_ID + ") VALUES ";
		for (int i = 0; i < tileIds.size(); i++) {
			int id = tileIds.get(i);
			if (i != 0)
				addTagRelation += ", ";
			addTagRelation += "('" + tagId + "', '" + id + "')";
		}
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(addTagRelation);
		
		log.info("Successfully set the new tag, '" + newTag + "', on " + tileIds.size() + "-many tiles.");
	}
	
	@Override
	public void changeTileTag(int id, String experiment, String newTag)
	{
		changeTileTags(Arrays.asList(new Integer[]{id}), experiment, newTag);
	}
	
	@Override
	public void removeTileTags(List<Integer> tileIds, String experiment)
	{
		log.info("Attempting to remove tags from " + tileIds.size() + "-many tiles.");
		
		String tileTable = addTileTable(experiment);
		
		String removeTiles = "DELETE FROM " + tileTable + " "
				+ " WHERE " + TILE_ID + " IN (" + StringOps.idsAsCSV(tileIds) + ")";
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(removeTiles);
		
		log.info("Successfully removed tags from " + tileIds.size() + "-many tiles.");
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Experiment Operations
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public void loadExperimentsWithTags(Collection<Experiment> experiments)
	{
		if (experiments.size() == 0)
			return;
		
		log.info("Checking for experiment metadata for " + experiments.size() + "-many experiments.");
		
		String getMetadata = "SELECT "
				+ NUMBER_SNAPSHOTS + ", "
				+ NUMBER_TILES + ", "
				+ LAST_UPDATED
				+ " FROM " + EXPERIMENT_TABLE
				+ " WHERE " + EXPERIMENT_ID + " IN (" + StringOps.idsAsCSV(Experiment.getIds(experiments)) + ") "; 
		
		JdbcTemplate metadataDatabase = new JdbcTemplate(metadataDataSource);
		metadataDatabase.query(getMetadata, new ExperimentMetadataLoader(experiments));
		
		log.info("Found experiments with metadata.");
	}
	
	@Override
	public int setExperimentMetadata(Collection<Experiment> experiments)
	{
		log.info("Attempting to update metadata for " + experiments.size() + "-many expirments.");
		
		String updateMetadata = "INSERT INTO " + EXPERIMENT_TABLE
				+ "( "
					+ EXPERIMENT_ID + ", "
					+ EXPERIMENT_NAME + ", "
					+ NUMBER_SNAPSHOTS + ", "
					+ NUMBER_TILES + ", "
					+ LAST_UPDATED
				+ ") "
				+ " VALUES "
					+ experimentSQLValues(experiments)
				+ " ON DUPLICATE KEY UPDATE "
				+ EXPERIMENT_NAME + " =VALUES(" + EXPERIMENT_NAME + "), "
				+ EXPERIMENT_ID   + " =VALUES(" + EXPERIMENT_ID   + ") ";
		
		JdbcTemplate metadataDatabse = new JdbcTemplate(metadataDataSource);
		int numberRowsChanged = metadataDatabse.update(updateMetadata);
		
		log.info("Successfully updated metadata for " + experiments.size() + "-many expirments.");
		
		return numberRowsChanged;
	}
	
	private static String experimentSQLValues(Collection<Experiment> experiments)
	{
		StringBuilder sqlValues = new StringBuilder();
		
		int i = 0;
		for (Experiment experiment : experiments) {
			
			if (i != 0)
				sqlValues.append(",");
			sqlValues.append(
					"( "
						+ experiment.id + ", "
						+ experiment.name + ", "
						+ experiment.numberSnapshots + ", "
						+ experiment.numberTiles + ", "
						+ experiment.lastUpdated
					+ ") ");
			
			i++;
		}
		
		return sqlValues.toString();
	}
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Setter / Getter Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	@Override
	public void setMetadataDataSource(DataSource database)
	{
		metadataDataSource = database;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Helper Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	private List<Integer> addTags(List<String> tags)
	{
		// Add the tags with ignore statement that does nothing if they already exist
		String addTagsQuery = "INSERT IGNORE INTO " + TAG_TABLE + " (" + TAG_NAME + ") VALUES ";
		
		for (int i = 0; i < tags.size(); i++) {
			String tag = tags.get(i);
			if (i != 0)
				addTagsQuery += ", ";
			addTagsQuery += "('" + tag + "')";
		}
		
		JdbcTemplate taggingDatabase = new JdbcTemplate(metadataDataSource);
		taggingDatabase.update(addTagsQuery);
		
		// Query the added tags for their IDs
		String getTagsQuery = "SELECT " + TAG_ID + " FROM " + TAG_TABLE + " "
				+ " WHERE " + TAG_NAME + " IN (";
		
		for (int i = 0; i < tags.size(); i++) {
			String tag = tags.get(i);
			if (i != 0)
				getTagsQuery += ", ";
			getTagsQuery += "'" + tag + "'";
		}
		getTagsQuery += ")"; 
		
		return taggingDatabase.query(getTagsQuery, new TagIdRowMapper());
	}
	
	private int addTag(String tag)
	{
		List<Integer> tagIds = addTags(Arrays.asList(new String[]{tag}));
		return tagIds.get(0);
	}
}
