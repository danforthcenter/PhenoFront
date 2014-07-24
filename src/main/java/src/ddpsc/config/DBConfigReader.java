package src.ddpsc.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.core.io.ClassPathResource;

import src.ddpsc.exceptions.MalformedConfigException;

/**
 * DBConfigReader reads the database.properties file used by the bean. The ExperimentConfig is not managed by spring
 *  so we need to load the properties ourself. 
 *  
 *  Expects properties to be '=' delimited, IE key=value
 *  Expects the file to exist in src/main/webapp
 * 
 * @field properties	hashmap which contains a dictionary of each property
 * @author shill
 *
 */
public final class DBConfigReader {

	String path = "database.properties"; //this needs to be a known global configuration file
	HashMap<String, String> properties = new HashMap<String, String>();
	DBConfigReader() throws MalformedConfigException{
		try {
			ClassPathResource c = new ClassPathResource("/");
			File f = c.getFile();
			FileReader reader = new FileReader(f.getAbsolutePath() + "/../../" + path);
			//FileReader reader = new FileReader(f);
			BufferedReader buff = new BufferedReader(reader);

			while( buff.ready() ){
				String curLine = buff.readLine();
				if (curLine.startsWith("#")){
					continue;
				}
				String[] lineArr = curLine.split("=", -1); //-1 lets us get empty results
				if (lineArr.length < 2){
					continue;
				} else{
					properties.put(lineArr[0], lineArr[1]);
				}
				
			}
			buff.close();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
