package src.ddpsc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

/**
 * Abstract construction of a class designed to read a flat yaml configuration file containing any relevant
 * configuration information for the PhenoFront web service.
 * 
 * The supplied configuration file expected to be one folder above the {@link ClassPathResource}.
 * 
 * The configuration files must be structured according to:
 * 		1) Lines beginning with '#' are ignored, treated as comments;
 * 		2) Empty lines are ignored;
 * 		3) Each non-comment, non-empty line must have two words separated by whitespace:
 * 					DATA_NAME <WHITESPACE> DATA_VALUE
 * 		   The first word is the name of the data, the second word is the value that variable takes.
 * 		   There is no definition for the type the data takes, that is interpreted by the user extending this class.
 * 		   
 * Lines with more than two words and a block of whitespace separating them log errors, but do not crash the system, or
 * throw exceptions.
 * 
 * All exceptions are expected to be thrown by the extending class, to be implemented by the user.
 * 
 * To extend this class, implement the method ProcessLine and store any desired configuration variables as
 * public variables (or create your own getters). See {@link LemnaTechDatabaseConfigReader} or {@link UserDatabaseConfigReader}
 * as examples.
 * 
 * @see ClassPathResource
 * @see LemnaTechDatabseConfigReader
 * @see UserDatabaseConfigReader
 * 
 * @author shill, cjmcentee
 *
 */
public abstract class ConfigReader
{
	private static final Logger log = Logger.getLogger(ConfigReader.class);
	
	public static final String COMMENT_CHARACTER = "#";
	
	protected abstract void ProcessLine(String name, String value);
	
	protected ConfigReader(String configurationFile)
	{
		
		try {
			ClassPathResource c = new ClassPathResource("/");
			File f = c.getFile();
			FileReader reader = new FileReader(f.getAbsolutePath() + "/" + configurationFile);
			BufferedReader buff = new BufferedReader(reader);
			// Flat file containing key pairs separated by an arbitrary amount of whitespace
			// First is name, second is value
			while( buff.ready() ) {
				String line = buff.readLine();
				
				// Exclude commented lines and empty lines
				if (line.startsWith(COMMENT_CHARACTER) || line.length() == 0)
					continue;
				
				// Split each line into its component words
				String[] words = line.split("\\s+", -1);
				
				// If there aren't two words, something went wrong, don't crash, but say something
				if (words.length != 2) {
					String configurationMessage = "Reading configuration file '" + configurationFile
							+ "' has encountered an invalid configuration line, tokenized as: {";
					for (int i = 0; i < words.length; i++) {
						configurationMessage += words[i];
						if (i != words.length - 1)
							configurationMessage += ", ";
					}
					configurationMessage += "}.";
					log.error(configurationMessage);
					continue;
				}
				
				// Process the variables according to the subclass's specifications
				String variableName = words[0];
				String variableValue = words[1];
				ProcessLine(variableName, variableValue);
			}
			
			buff.close();
		}
		catch (FileNotFoundException e) {
			String noConfigMessage = "No configuration file could be found at '" + configurationFile + "'";
			log.error(noConfigMessage, e);
			return;
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
}
