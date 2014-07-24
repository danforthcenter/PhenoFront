package src.ddpsc.authentication;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
	private static final Logger log = Logger.getLogger(CustomAuthenticationManager.class);

	@Autowired
	private UserDao userDao;
	private PasswordEncoder passwordEncoder = new StandardPasswordEncoder();

	/**
	 * Determines whether the authentication object has valid credentials for logging into this webservice.
	 * 
	 * @param	Authentication		The current user's login credentials
	 * @return						A successful authentication token with the appropriate user authority
	 * 
	 * @throws	AuthenticationException		Thrown if something is invalid with the current authentication credentials
	 */
	public Authentication authenticate(Authentication auth) throws AuthenticationException
	{
		try {
			log.info("Attempting to authenticate the user " + auth.getName());
			
			String username = auth.getName();
			DbUser user = userDao.findByUsername(username);
			
			if ( ! user.isEnabled())
				throw new DisabledException("The account for the user " + username + " is disabled.");
			
			// Check if the user input password matches the password found in the user-data database
			boolean passwordValid = passwordEncoder.matches((CharSequence) auth.getCredentials(), user.getPassword());
			if (passwordValid) {
				log.info("Authentication Successful for the user " + user.getUsername() + ", generating token.");
				return new UsernamePasswordAuthenticationToken(username, auth.getCredentials(), getAuthorities(user));
			}
			
			else
				throw new BadCredentialsException("Invalid password for the username '" + username + "'");
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
		
		
	}

	/**
	 * Utility function for things that want to check password matching outside
	 * of the security contexts.
	 * 
	 * @param user			The user whose password is being checked
	 * @param rawPassword	The raw input password, probably typed by the website user
	 * @return				Whether the raw password matches the user's password
	 */
	public static boolean validateCredentials(String rawPassword, DbUser user)
	{
		PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
		
		boolean passwordsMatch = passwordEncoder.matches((CharSequence) rawPassword, user.getPassword());
		return passwordsMatch;
	}

	/**
	 * Retrieves the correct ROLE type depending on the user's access level, where
	 * access level is an Integer.
	 * 
	 * @param		access		An integer value representing the access of the user
	 * @return 					A collection of granted authorities
	 */
	public Collection<GrantedAuthority> getAuthorities(DbUser user)
	{
		Set<GrantedAuthority> authList = new HashSet<GrantedAuthority>();
		
		// Everyone is at least a user
		authList.add(new SimpleGrantedAuthority(DbUser.USER));
		
		
		if (user.isAdmin()) {
			log.info("Granted the authority of: " + DbUser.ADMIN + " to the user " + user.getUsername());
			authList.add(new SimpleGrantedAuthority(DbUser.ADMIN));
		}
		
		return authList;
	}

}