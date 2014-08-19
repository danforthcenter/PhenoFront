package src.ddpsc.database.queries;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

public interface QueryDao
{
	void setMetadataDataSource(DataSource dataSource);
	
	List<Query> getQueries(QueryFilter queryFilter);
	
	int addQuery(Query query) throws SQLException;
	
	void setQueryComment(int queryId, String newComment);
	void setQueryComments(List<Integer> queryIds, String newComment);
	
	void setDownloadStart(int queryId, Timestamp time);
	void setDownloadEnd(int queryId, Timestamp time);
	void setInterrupted(int queryId, boolean wasInterrupted);
	void setQuerySize(int queryId, long bytes);
}
