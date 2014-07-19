package src.ddpsc.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import src.ddpsc.database.user.UserDao;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.exceptions.UserException;
import src.ddpsc.exceptions.ObjectNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Our custom authentication manager. Nothing to complex goes on, just password
 * matching using StandardPasswordEncoder.
 * 
 * @throws	BadCredentialsException
 * @throws	CannotGetJdbcConnectionException
 */
public class CustomAuthenticationManager implements AuthenticationManager
{
	private static Logger log = Logger.getLogger("service");

	@Autowired
	private UserDao userDao;
	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	public Authentication authenticate(Authentication auth) throws AuthenticationException
	{
		String username = auth.getName();
		DbUser user = null;
		
		try {
			user = userDao.findByUsername(username);
		}
		catch (CannotGetJdbcConnectionException e) {
			throw new AuthenticationServiceException(e.getMessage());
		}
		catch (UserException e) {
			throw new AuthenticationServiceException(e.getMessage());
		}
		catch (ObjectNotFoundException e) {
			throw new AccountExpiredException(e.getMessage());
		}
		
		if ( ! user.isEnabled()) {
			String disabledUserMessage = "The account for user '" + username + "' is disabled.";
			log.error(disabledUserMessage);
			throw new DisabledException(disabledUserMessage);
		}
		
		// Check if the user input password matches the password found in the user-data database
		boolean passwordValid = passwordEncoder.matches((CharSequence) auth.getCredentials(), user.getPassword());
		if (passwordValid) {
			log.trace("Authentication Successful for '" + user.getUsername() + "', generating token.");
			return new UsernamePasswordAuthenticationToken(username, auth.getCredentials(), getAuthorities(user));
		}
		
		else {
			String invalidErrorMessage = "Invalid password for the username '" + username + "'";
			log.trace(invalidErrorMessage);
			throw new BadCredentialsException(invalidErrorMessage);
		}
	}

	/**
	 * Utility function for things that want to check password matching outside
	 * of the security contexts
	 * 
	 * @param user
	 * @param rawPassword
	 * @return
	 */
	public static boolean validateCredentials(String rawPassword, DbUser user)
	{
		PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		
		boolean passwordsMatch = passwordEncoder.matches((CharSequence) rawPassword, user.getPassword());
		return passwordsMatch;
	}

	/**
	 * Retrieves the correct ROLE type depending on the access level, where
	 * access level is an Integer. Basically, this interprets the access value
	 * whether it's for a regular user or admin.
	 * 
	 * @param access
	 *            an integer value representing the access of the user
	 * @return collection of granted authorities
	 */
	public Collection<GrantedAuthority> getAuthorities(DbUser user)
	{
		// Create a list of grants for this user
		List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>(2);
		authList.add(new SimpleGrantedAuthority("ROLE_USER"));
		if (user.getAuthority().equals("ROLE_ADMIN")) {
			log.debug("Grant ROLE_ADMIN to this user");
			authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		}
		return authList;
	}

}