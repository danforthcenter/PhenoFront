package src.ddpsc.database.user;

/**
 * Class responsible for managing groups.
 * 
 * Groups contain some number of users, and users that are contained in a group
 * have permission to view and work on different experiments. Users may only belong to one group for now, this
 * may be changed in the future, however to do this, there needs to be an additional relation table. Groups also have
 * owners, typically this will be the PI.
 * 
 * @author shill, cjmcentee
 */
public class DbGroup
{
	
	private int		groupID;
	private String	groupName;
	private DbUser	owner;
	
	
	public DbGroup()
	{
	}
	
	public DbGroup(int groupID, String groupName, DbUser owner)
	{
		this.groupID = groupID;
		this.groupName = groupName;
		this.owner = owner;
	}
	
	
	public boolean isOwner(DbUser owner)
	{
		if(owner.getUsername().equals(this.owner))
			return true;
		else
			return false;
	}
	
	public String describe()
	{
		StringBuilder groupDescription = new StringBuilder();
		
		groupDescription.append(groupName == null ? "<NULL NAME>" : groupName);
		groupDescription.append(" with ID " + groupID + " owned by ");
		groupDescription.append(owner == null ? "<NULL OWNER>" : (owner.getUsername() == null ? "<NULL OWNER NAME>" : owner.getUsername()));
		
		return groupDescription.toString();
	}
	
	@Override
	public String toString() {
		return "Group ["
				+ "groupId="	+ groupID + ", "
				+ "groupName="	+ groupName + ", "
				+ "owner="		+ (owner == null ? "<NULL OWNER>" : owner.getUsername())
				+ "]";
	}

	public int getGroupID()
	{
		return groupID;
	}

	public void setGroupID(int groupID)
	{
		this.groupID = groupID;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public DbUser getOwner()
	{
		return owner;
	}

	public void setOwner(DbUser owner)
	{
		this.owner = owner;
	}
}
