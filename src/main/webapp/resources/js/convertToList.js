/**
 *  Converts a string representation of a java list into an actual javascript list.
 *  
 *  Example Java List:
 *  String[] javaList = new String[]{"this", "is", "some", "shameful", "shit"};
 *  model.add("javaList", javaList);
 *  
 *  And after being added to the model and called by Javascript:
 *  var newList = ${javaList};
 *  
 *  What you actually get is the string, and not an array of any form:
 *  "[this, is, some, shameful, shit]"
 *  
 *  This method takes in that string and converts it to an array.
 *  
 *  
 *  @author cjmcentee
 */
function convertToList(listAsString)
{
	// "[this, is, some, shameful, shit]"
	// Remove the brackets
	csvAsString = listAsString.substring(1, listAsString.length-1);
	
	// "this, is, some, shameful, shit"
	// Split by ", "
	return csvAsString.split(", ");
}
