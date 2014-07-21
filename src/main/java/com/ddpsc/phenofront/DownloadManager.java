package com.ddpsc.phenofront;

import java.util.Random;

import src.ddpsc.database.user.DbUser;
import src.ddpsc.exceptions.ActiveKeyException;

/**
 * Essentially static class which is responsible for limiting users to 1
 * download each, also allows an interface for anonymous downloads. Users
 * checkout a key and then download with this random key.
 * 
 * Each user has two entries, username => activeKey, key => username Essentially
 * a two way interface for downlaod codes. If a key is active, we will not use
 * it. When a download is started, set the key to active so that user may not
 * use any more keys. A key will not be added if the user already has a download
 * in progress
 * 
 * http://stackoverflow.com/questions/3679260/how-can-you-tell-if-the-user-hit-cancel-during-a-download-from-a-java-servlet
 * Deprecating this class until that is handled
 * 
 * 
 * 
 * @author shill
 * 
 */
public class DownloadManager {
	/**
	 * 
	 * @return Returns the key as stored in memory.
	 * 
	 * @throws ActiveKeyException	If the user already has an active key, an exception is thrown.
	 * 
	 * TODO: Actually throw ActiveKeyException
	 */
	public static String generateRandomKey(DbUser user)
	{
			Random rand = new Random(System.currentTimeMillis());
			
			String theDigest = Long.toString(rand.nextLong());
			
			System.setProperty(theDigest, user.getUsername());
			return theDigest;
	}

	/**
	 * Sets the key to active.
	 * @deprecated
	 * @param key
	 * @return Returns the user which now has an active key.
	 * @throws ActiveKeyException
	 *             If the key does not exist an exception is thrown, if the user
	 *             already has an active key an exception is thrown.
	 */
	public static String setKeyActive(String key) throws ActiveKeyException {
		String username = System.getProperty(key);
		if (username == null) {
			throw new ActiveKeyException("Key does not exist: " + key);
		}
		if (System.getProperty(username) != null) {
			throw new ActiveKeyException("Key exists for user: " + username);
		} else {
			System.setProperty(username, key);
			return username;
		}
	}

	/**
	 * Sets the key for the given user as inactive. Removes the key from the system.
	 * If the key does not exist then it returns nothing and silently continues.
	 * @deprecated
	 * @param key
	 * @return

	 */
	public static String setKeyInactive(String key) {
		String username = System.getProperty(key);
		if (username == null) {
			return "";
		}
		if (System.getProperty(username) == null) {
			System.clearProperty(key);
			return "";
		} else {
			System.clearProperty(username);
			System.clearProperty(key);
			return username;
		}
	}

	public static String getKey(DbUser user) {
		return 	System.getProperty(user.getUsername());
	}
}
