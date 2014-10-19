package src.ddpsc.results;

import java.util.List;

public class DownloadZipResult
{
	public final long size;
	public final List<Integer> missedSnapshots;
	public final boolean succeeded;
	
	public DownloadZipResult(long size, List<Integer> missedSnapshots, boolean succeeded)
	{
		this.size = size;
		this.missedSnapshots = missedSnapshots;
		this.succeeded = succeeded;
	}
}
