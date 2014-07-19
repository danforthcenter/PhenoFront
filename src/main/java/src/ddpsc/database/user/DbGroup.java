package src.ddpsc.database.user;

/**
 * Class responsible for managing groups. 
 * 
 * Groups contain some number of users, and users that are contained in a group
 * have permission to view and work on different experiments. Users may only belong to one group for now, this
 * may be changed in the future, however to do this, there needs to be an additional relation table. Groups also have
 * owners, typically this will be the PI.
 * 
 * @author shill
 *
 */
public class DbGroup {
	
	private int		groupId;
	private String	groupName;
	private DbUser	owner;
	
	
	public DbGroup() {
	}
	
	public DbGroup(int groupId, String groupName, DbUser owner) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.owner = owner;
	}
	
	
	public boolean isOwner(DbUser query){
		if(query.getUsername().equals(this.owner)){
			return true;
		}
		else return false;
	}
	
	public String shortDescribe()
	{
		return "group " + groupName + " with ID='" + groupId + "'";
	}
	
	public String describe()
	{
		StringBuilder groupDescription = new StringBuilder();
		
		groupDescription.append("Group ");
		groupDescription.append(groupName == null ? "<NULL NAME>" : groupName);
		groupDescription.append("with ID " + groupId + " owned by ");
		groupDescription.append(owner == null ? "<NULL OWNER>" : (owner.getUsername() == null ? "<NULL OWNER NAME>" : owner.getUsername()));
		
		return groupDescription.toString();
	}
	
	@Override
	public String toString() {
		return "Group [groupId=" + groupId
				+ ", groupName=" + groupName
				+ ", owner=" + owner.getUsername() + "]";
	}
	
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public DbUser getOwner() {
		return owner;
	}
	public void setOwner(DbUser owner) {
		this.owner = owner;
	}
}
