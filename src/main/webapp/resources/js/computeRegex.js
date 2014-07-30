/**
 * Takes in a regex pattern (as a string), and a list of barcodes and outputs
 * a list of barcodes that have a match according to the regex pattern.
 * 
 * @author cjmcentee
 */
function computeRegex(pattern, barcodes)
{
	var regex;
	try {
		regex = new RegExp(pattern);
	}
	catch (e) {
		// Invalid regex
		return "Invalid regex.";
	}
	
	var matchedBarcodes = [];
	
	barcodes.forEach(function(barcode) {
		if (regex.test(barcode))
			matchedBarcodes.push(barcode);
	});
	
	return neatString(matchedBarcodes);
}

// Converts to a neater string than .toString() does
function neatString(stringList)
{
	var concatString = "";
	stringList.forEach(function(str) {
		concatString += str + ", ";
	});
	
	return concatString;
}