package src.ddpsc.database.tagging;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.ddpsc.config.ConfigReader;
import src.ddpsc.exceptions.MalformedConfigException;
import src.ddpsc.utility.Tuple;


/**
 * Reads a series of metadata modifications out of a file.
 * 
 * The file is expected to be uploaded by the user.
 * 
 * The format of the file is a whitespace separated table of two columns, the left column
 * identifies the metadata entry to modifer, and the second column is the new metadata entry.
 * 
 * The first (or only) column on a line identifies the metadata to change by the type
 * 'query', 'snapshot', or 'tile', followed by the ID number. It is expected to be of the form:
 * 				type#id  			(note the '#' in the middle)
 * Specifically it uses the regex '^(snapshot|query|tile)#(\\d+)$' against the isolated string.
 * 
 * If the first column is the only column, the lack of a second column is interpreted to mean the metadata
 * for the identified entry should be deleted.
 * 
 * The second column is the new metadata which can be an arbitrary string but must stay on a single line.
 * 
 * And file that modifies the tags on snapshots or tiles must also have the two column row defining the experiment:
 * 			EXPERIMENT_NAME		actualExperimentName
 * Where "EXPERIMENT_NAME" is a constant that shouldn't be changed and "actualExperimentName" is the user typed experiment name
 * corresponding to the snapshots that are to be changed. Any changes to snapshots in multiple experiments, therefore, requires
 * multiple files.
 * 
 * 
 * The format of the file is:
 * 	VALID:
 * 		- Single word lines of the form (snapshot|query|tile)#(\\d+) are treated as a delete metadata command
 * 		- Two word lines with the first word of the form (snapshot|query|tile)#(\\d+) are treated as set metadata commands
 * 	IGNORED:
 * 		- Empty lines are ignored
 * 		- Lines beginning with '#' are ignored
 * 	ERROR:
 * 		- Lines with more than 2 whitespace separated strings throw errors
 * 		- Lines where the first (or only) string isn't of the form: (snapshot|query|tile)#(\\d+) throws an error
 * 
 * @author cjmcentee
 */
public class MetadataFileReader extends ConfigReader
{
	private static final Pattern idComposition = Pattern.compile("^(snapshot|query|tile)#(\\d+)$");
	
	public String experiment;
	
	public Map<Integer, String> queryCommentChanges;
	public Map<Integer, String> snapshotTagChanges;
	public Map<Integer, String> tileTagChanges;
	
	/**
	 * Creates a new TaggingFileReader object that contains all the values defined
	 * in the user's uploaded tagging file.
	 * 
	 * @throws MalformedConfigException			Thrown when the config file is incomplete
	 */
	public MetadataFileReader(BufferedReader file) throws MalformedConfigException
	{
		super(file);
		
		queryCommentChanges = new HashMap<Integer, String>();
		snapshotTagChanges = new HashMap<Integer, String>();
		tileTagChanges = new HashMap<Integer, String>();
		
		processFile();
		
		if (snapshotTagChanges.size() > 0 || tileTagChanges.size() > 0)
			if (experiment == null || experiment.equals(""))
				throw new MalformedConfigException("Metadata change file that modifies snapshots or tiles must define an experiment.");
	}
	
	protected void processColumn(String singleColumn) throws MalformedConfigException
	{
		processColumns(singleColumn, ""); // Empty metadata entry will be interpreted as a delete command
	}
	
	protected void processColumns(String name, String value) throws MalformedConfigException 
	{
		// EXPERIMENT_NAME line
		if (name.equals("EXPERIMENT_NAME")) {
			if (experiment == null || experiment.equals(""))
				this.experiment = value;
			else // experiment already set, the file should be thrown back to ensure the user knows what they're doing
				throw new MalformedConfigException("The experiment cannot be set twice in the config. First value: " + experiment + " and second value: " + value);
			return;
		}
			
		// Metadata modification line
		Tuple<String, Integer> metadataIdentifier = parseMetadataIdentifier(name.toLowerCase());
		
		String metadataType = metadataIdentifier.fst;
		int id = metadataIdentifier.snd;
		String metadata = value;
		
		if (metadataType.equals("query"))
			queryCommentChanges.put(id, metadata);
		
		else if (metadataType.equals("snapshot"))
			snapshotTagChanges.put(id, metadata);
		
		else if (metadataType.equals("tile"))
			tileTagChanges.put(id, metadata);
		
		else
			throw new MalformedConfigException("Metadata type '" + metadataType + "' not one of 'query', 'snapshot', or 'tag'");
	}
	
	protected void processLine(String line) throws MalformedConfigException
	{
		System.out.println("LINE " + line);
		
		// CASE: Line has no whitespaces making it a single column
		if ( ! Pattern.matches(".*\\s+.*", line)) {
			processColumn(line);
			return;
		}
		
		// CASE: Line has more than one column, treat the first one as the name of the metadata
		// And the remaining columns are the new metadata
		// I tried using Regex but java's regex system is FUBAR. More hacking!
		String[] tokens = line.split("\\s+");
		String metadataIdentifier = tokens[0];
		
		// Remove the metadata identifier from the line and what remains is the metadata itself
		String metadata = line.substring(metadataIdentifier.length()).trim();
		
		processColumns(metadataIdentifier, metadata);
	}
	
	private Tuple<String, Integer> parseMetadataIdentifier(String identifyingString) throws MalformedConfigException
	{
		Matcher nameMatch = idComposition.matcher(identifyingString);
		
		if (nameMatch.matches() == false)
			throw new MalformedConfigException("Variable identifier '" + identifyingString
					+ "' invalid. Must be of form: (snapshot|query|tile)#(\\d+)");
		
		MatchResult tokenize = nameMatch.toMatchResult();
		
		String metadataType = tokenize.group(1);
		int id = Integer.parseInt(tokenize.group(2));
		
		return new Tuple<String, Integer>(metadataType, id);
	}
}
