package com.ddpsc.phenofront;

public class MySQLColumn
{
	// Types
	public static String VARCHAR(int d) {			return "varchar("+d+")"; }
	
	public static String BIGINT(int d) {			return "bigint("+d+")"; }
	public static String INT(int d) {				return "int("+d+")"; }
	public static String INT_UNSIGNED(int d) {		return "int("+d+") unsigned"; }
	public static String TINYINT(int d) {			return "tinyint("+d+")"; }
	public static String BOOLEAN() {				return "tinyint(1)"; }
	
	public final static String DATETIME				= "datetime";
	public final static String TEXT					= "text";
	
	// Keys
	public final static String PRIMARY_KEY			= "PRI";
	public final static String UNIQUE_KEY			= "UNI";
	public final static String MULTIPLE_KEY			= "MUL";
	public final static String NOT_KEY				= "";
	
	// Nullity settings
	public final static boolean NEVER_NULL			= false;
	public final static boolean	CAN_NULL			= true;
	
	
	
	public final String field, type, keyType;
	public final boolean canBeNull;
	
	public MySQLColumn(String field, String type, boolean canBeNull, String keyType)
	{
		this.field = field;
		this.type = type;
		this.canBeNull = canBeNull;
		this.keyType = keyType;
	}
	
	@Override
	public int hashCode()
	{
		return super.hashCode() + 3*field.hashCode() + 7*type.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj != null && obj instanceof MySQLColumn)
			return equals((MySQLColumn) obj);
		else
			return false;
	}
	
	public boolean equals(MySQLColumn col)
	{
		// Equal regardless of case
		return field.toLowerCase().equals(col.field.toLowerCase())
			&& type.toLowerCase().equals(col.type.toLowerCase())
			&& keyType.toLowerCase().equals(col.keyType.toLowerCase())
			&& canBeNull == col.canBeNull;
	}
}
