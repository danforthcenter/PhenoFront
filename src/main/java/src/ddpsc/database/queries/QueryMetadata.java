package src.ddpsc.database.queries;

import java.sql.Timestamp;
import java.util.List;


public class QueryMetadata
{
	public final int userId;
	public final String username;
	public final Timestamp dateMade;
	public final int numberSnapshots;
	public final int numberTiles;
	
	public String comment;
	
	public Timestamp downloadBegin;
	public Timestamp downloadEnd;
	public boolean interrupted;
	public List<Integer> missedSnapshots;
	
	public long bytes;
	
	QueryMetadata()
	{
		this.userId = 0;
		this.username = null;
		this.dateMade = null;
		
		this.numberSnapshots = 0;
		this.numberTiles = 0;
		
		this.comment = null;
	}
	
	public QueryMetadata(
			int userId,
			String username,
			Timestamp dateMade,
			int numberSnapshots,
			int numberTiles,
			String comment )
	{
		this.userId = userId;
		this.username = username;
		this.dateMade = dateMade;
		
		this.numberSnapshots = numberSnapshots;
		this.numberTiles = numberTiles;
		
		this.comment = comment;
	}
	
	
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	// Get / Set Methods
	// ////////////////////////////////////////////////
	// ////////////////////////////////////////////////
	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public Timestamp getDownloadBegin()
	{
		return downloadBegin;
	}

	public void setDownloadBegin(Timestamp downloadBegin)
	{
		this.downloadBegin = downloadBegin;
	}

	public Timestamp getDownloadEnd()
	{
		return downloadEnd;
	}

	public void setDownloadEnd(Timestamp downloadEnd)
	{
		this.downloadEnd = downloadEnd;
	}

	public boolean isInterrupted()
	{
		return interrupted;
	}

	public void setInterrupted(boolean interrupted)
	{
		this.interrupted = interrupted;
	}

	public List<Integer> getMissedSnapshots()
	{
		return missedSnapshots;
	}

	public void setMissedSnapshots(List<Integer> missedSnapshots)
	{
		this.missedSnapshots = missedSnapshots;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public int getUserId()
	{
		return userId;
	}

	public String getUsername()
	{
		return username;
	}

	public Timestamp getDateMade()
	{
		return dateMade;
	}

	public int getNumberSnapshots()
	{
		return numberSnapshots;
	}

	public int getNumberTiles()
	{
		return numberTiles;
	}
}
