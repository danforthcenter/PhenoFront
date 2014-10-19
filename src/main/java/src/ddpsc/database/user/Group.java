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
public class Group
{
	
	public static final String GROUP_ID =	"group_id";
	public static final String OWNER_ID =	"owner_id";
	public static final String GROUP_NAME =	"group_name";
	
	private int		groupId;
	private String	groupName;
	private User	owner;
	
	
	public Group()
	{
	}
	
	public Group(int groupId, String groupName, User owner)
	{
		this.groupId = groupId;
		this.groupName = groupName;
		this.owner = owner;
	}
	
	
	public boolean isOwner(User owner)
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
		groupDescription.append(" with ID " + groupId + " owned by ");
		groupDescription.append(owner == null ? "<NULL OWNER>" : (owner.getUsername() == null ? "<NULL OWNER NAME>" : owner.getUsername()));
		
		return groupDescription.toString();
	}
	
	@Override
	public String toString() {
		return "Group ["
				+ "groupId="	+ groupId + ", "
				+ "groupName="	+ groupName + ", "
				+ "owner="		+ (owner == null ? "<NULL OWNER>" : owner.getUsername())
				+ "]";
	}

	public int getGroupId()
	{
		return groupId;
	}

	public void setGroupId(int groupId)
	{
		this.groupId = groupId;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public User getOwner()
	{
		return owner;
	}

	public void setOwner(User owner)
	{
		this.owner = owner;
	}
}
