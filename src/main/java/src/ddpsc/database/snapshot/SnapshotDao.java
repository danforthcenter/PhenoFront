package src.ddpsc.database.snapshot;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.EnumSet;
import java.util.List;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

import src.ddpsc.database.queries.Query;
import src.ddpsc.database.tagging.TaggingDao;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.exceptions.ObjectNotFoundException;

/**
 * Required interface for interacting with the database. Implemented by the corresponding impl Class.
 * 
 * @see SnapshotDaoImpl
 * 
 * @author shill, cjmcentee
 * 
 */
public interface SnapshotDao
{
	public void setSnapshotExperiment(String experimentName) 	throws MalformedConfigException, IOException;
	
	
	public Snapshot findById(int id) throws CannotGetJdbcConnectionException, ObjectNotFoundException;
	public List<Snapshot> findById(List<Integer> ids) throws CannotGetJdbcConnectionException, ObjectNotFoundException;
	public List<Snapshot> findAfterTimestamp(Timestamp timestamp) throws CannotGetJdbcConnectionException;
	public List<Snapshot> findBetweenTimes(Timestamp startTime, Timestamp endTime) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> executeCustomQuery(Query querySettings) throws CannotGetJdbcConnectionException;
}
