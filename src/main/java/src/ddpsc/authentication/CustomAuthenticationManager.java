package src.ddpsc.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import src.ddpsc.database.user.UserDao;
import src.ddpsc.database.user.DbUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Our custom authentication manager. Nothing to complex goes on, just password matching using 
 * StandardPasswordEncoder.
 *  
 * @throws {@link BadCredentialsException}
 */
public class CustomAuthenticationManager implements AuthenticationManager {

	protected static Logger logger = Logger.getLogger("service");
	@Autowired
	private UserDao userDao;

	private StandardPasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		DbUser user = null;
		try {
			// Retrieve user details from database
			user = userDao.findByUsername(auth.getName());
		} catch (Exception e) {
			logger.error("User does not exist: " + auth.getName());
			e.printStackTrace();
			throw new BadCredentialsException("User does not exist: " + auth.getName());
		}

		if (passwordEncoder.matches((CharSequence) auth.getCredentials(),
				user.getPassword())) {
			logger.info("Authentication Successful, generating token.");
			return new UsernamePasswordAuthenticationToken(auth.getName(),
					auth.getCredentials(), getAuthorities(user));
		}
		//authentication failed
		logger.error("Passwords do not match");
		throw new BadCredentialsException("Passwords do not match!");

	}
	  
	 /**
	  * Retrieves the correct ROLE type depending on the access level, where access level is an Integer.
	  * Basically, this interprets the access value whether it's for a regular user or admin.
	  * 
	  * @param access an integer value representing the access of the user
	  * @return collection of granted authorities
	  */
	public Collection<GrantedAuthority> getAuthorities(DbUser user) {
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getAuthority().equals("ROLE_ADMIN")){
			logger.debug("Grant ROLE_ADMIN to this user");
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return authList;
	}
	
}