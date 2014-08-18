package src.ddpsc.resumeDownloadApplet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CSV
{
	List<String> labels;
	List<List<String>> rows;
	
	public CSV(String labels, List<String> rows) throws MalformedCSVException
	{
		this.labels = csvToList(labels);
		this.rows = new ArrayList<List<String>>();
		
		for (String row : rows)
			add(csvToList(row));
	}
	
	public CSV(CSV csv) throws MalformedCSVException
	{
		this.labels = new ArrayList<String>(csv.labels);
		this.rows = new ArrayList<List<String>>();
		
		for (List<String> row : csv.rows)
			add(new ArrayList<String>(row));
	}
	
	public void add(List<String> row) throws MalformedCSVException
	{
		if (row.size() != labels.size())
			throw new MalformedCSVException(" The row " + collectionToCSV(row)
					+ " does not match the labels " + collectionToCSV(labels) + ".");
		
		this.rows.add(row);
	}
	
	public void remove(String variableName, String variableValue)
	{
		List<String> removalRow = null;
		
		int variableIndex = labels.indexOf(variableName);
		for (List<String> row : rows) {
			String rowVariable = row.get(variableIndex);
			boolean isRowToRemove = rowVariable.equals(variableValue); 
			if (isRowToRemove) {
				removalRow = row;
				break;
			}
		}
		
		rows.remove(removalRow);
	}
	
	public String get(String variableName, int rowIndex) throws MalformedCSVException
	{
		try {
			int valueIndex = labels.indexOf(variableName);
			
			if (valueIndex == -1)
				throw new MalformedCSVException("Label " + variableName + " does not exist.");
			
			return rows.get(rowIndex).get(valueIndex);
		}
		catch (IndexOutOfBoundsException e) {
			throw new MalformedCSVException("Row " + rowIndex + " does not have a value for label " + variableName);
		}
	}
	
	public int numRows()
	{
		return rows.size();
	}
	
	@Override
	public String toString()
	{
		StringBuilder csv = new StringBuilder("");
		
		csv.append(collectionToCSV(labels) + "\n");
		
		for (List<String> row : rows)
			csv.append(collectionToCSV(row) + "\n");
		
		return csv.toString();
	}
	
	public void saveTo(File file) throws FileNotFoundException
	{
		PrintWriter output = new PrintWriter(file);
		output.write(this.toString());
		output.close();
	}
	
	public static List<String> csvToList(String csv)
	{
		List<String> valueList = new ArrayList<String>(Arrays.asList(csv.split(",")));
		
		// If the CSV ends with a comma, split doesn't pick up the trailing empty string, so check manually
		String lastCharacter = csv.substring(csv.length()-1, csv.length());
		if (lastCharacter.equals(","))
			valueList.add("");
		
		return valueList;
	}
	
	public static String collectionToCSV(Collection<String> stringCollection)
	{
		if (stringCollection.size() == 0)
			return "";
		
		StringBuilder csvBuilder = new StringBuilder("");
		for (String str : stringCollection)
			csvBuilder.append(str + ","); // Will add a trailing "," to be removed later
		
		String csv = csvBuilder.toString();
		// Remove the trailing ","
		return csv.substring(0, csv.length() - 1);
	}
}