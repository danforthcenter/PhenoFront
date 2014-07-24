package src.ddpsc.database.snapshot;

import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.CannotGetJdbcConnectionException;

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
	public void setDataSource(DataSource dataSource);
	
	
	
	public List<String> getBarcodes(int maxTags) throws CannotGetJdbcConnectionException;
	
	public List<String> getMeasurementLabels(int maxTags) throws CannotGetJdbcConnectionException;
	
	
	
	public Snapshot findByID(int id) throws CannotGetJdbcConnectionException, ObjectNotFoundException;
	
	public Snapshot findByID_withTiles(int id) throws CannotGetJdbcConnectionException, ObjectNotFoundException;
	
	
	
	public List<Snapshot> findAfterTimestamp(Timestamp timestamp) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findAfterTimestamp_withTiles(Timestamp timestamp) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findAfterTimestamp_imageJobs(Timestamp timestamp) throws CannotGetJdbcConnectionException;
	
	
	
	public List<Snapshot> findBetweenTimes(Timestamp startTime, Timestamp endTime) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findBetweenTimes_withTiles(Timestamp startTime, Timestamp endTime) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findBetweenTimes_imageJobs(Timestamp startTime, Timestamp endTime) throws CannotGetJdbcConnectionException;
	
	
	
	public List<Snapshot> findLastN(int n) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findLastN_withTiles(int n) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findLastN_imageJobs(int n) throws CannotGetJdbcConnectionException;
	
	
	
	public List<Snapshot> findCustomQueryAnyTime_imageJobs(Timestamp startTime, Timestamp endTime, String plantBarcode, String measurementLabel) throws CannotGetJdbcConnectionException;
	
	public List<Snapshot> findCustomQueryAnyTime_imageJobs_withTiles(Timestamp startTime, Timestamp endTime, String plantBarcode, String measurementLabel) throws CannotGetJdbcConnectionException;
}
