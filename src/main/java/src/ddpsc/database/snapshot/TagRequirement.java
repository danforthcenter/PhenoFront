package src.ddpsc.database.snapshot;

public enum TagRequirement
{
	All,
	OneOf,
	None;
	
	public static TagRequirement fromString(String requirementString)
	{
		if (requirementString.equals("All"))
			return TagRequirement.All;
		
		if (requirementString.equals("OneOf"))
			return TagRequirement.OneOf;
		
		if (requirementString.equals("None"))
			return TagRequirement.None;
		
		throw new IllegalArgumentException();
	}
}
