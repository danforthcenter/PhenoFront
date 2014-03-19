package src.ddpsc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.core.io.ClassPathResource;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * Class designed to read a flat yaml configuration file containing information about the LT databases
 *   skip lines that begin with # and assumes they are comments.
 *   The required configs are databaseUsername, databasePass, and databaseURL. dbDefault is the default
 *   database to connect to.
 *   It could be better to rework this class so it can be used in a static manner.
 * 
 * The file it looks for is in the folder immediately above the ClassPathResource. The name is ltdatabase.conf
 *  This file is not included in the main git repository, however an example file is concluded.
 * 
 * @author shill
 *
 */
public final class DBConfigReader {
	String dbUsername;
	String dbPass;
	String dbURL;
	String dbDefault = "";
	String path = "ltdatabase.conf"; //this needs to be a known global configuration file
	DBConfigReader() throws MalformedConfigException{
		try {
			ClassPathResource c = new ClassPathResource("/");
			File f = c.getFile();
			FileReader reader = new FileReader(f.getAbsolutePath() + "/../" + path);
			BufferedReader buff = new BufferedReader(reader);
			//flat file containing key pairs
			//first is name, second is value (ez)
			while( buff.ready() ){
				String curLine = buff.readLine();
				if (curLine.startsWith("#")){
					continue;
				}
				String[] lineArr = curLine.split("\t", -1); //-1 lets us get empty results
				if (lineArr.length < 2){
					continue;
				}
				if (lineArr[0].equals("databaseUsername")){
					this.dbUsername = lineArr[1];
				}
				else if (lineArr[0].equals("databasePass")){
					this.dbPass = lineArr[1];
				}
				else if (lineArr[0].equals("databaseURL")){
					this.dbURL = lineArr[1];
				}
				else if(lineArr[0].equals("dbDefault")){
					this.dbDefault = lineArr[1];
				}
			}
			buff.close();
			if (this.dbUsername == null || this.dbPass == null || this.dbURL == null ){
				throw new MalformedConfigException("One or more of the required fields are missing. Check your LTdatabase config file.");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
