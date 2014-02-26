package com.ddpsc.phenofront;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import javax.servlet.http.HttpSession;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import src.ddpsc.database.user.UserDao;

/**
 * Class designed for testing authentication and authorization of main
 * controllers. Because most of the work is done by the spring security layer,
 * tests for each secured controller are not implemented (redundancy).
 * 
 * @author shill
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/testAuthContext.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
public class AuthenticationSuiteTest extends TestUtility {

	private MockMvc mockMvc;

	@Autowired
	private FilterChainProxy springSecurityFilter;

	@Autowired
	private WebApplicationContext webappContext;

	private TestUtility tu;

	@Autowired
	private UserDao userDaoMock;

	@Autowired
	MockHttpSession session;

	/**
	 * Setup our tests with the general purpose users and the web context.
	 * 
	 * @author shill
	 */
	@Before
	public void setUp() {
		UserTestUtility();
		this.mockMvc = webAppContextSetup(this.webappContext).addFilter(
				this.springSecurityFilter, "/*").build();
	}

	/**
	 * Simple login test. Checks that a user with valid credentials can login
	 * and is redirected to /userarea
	 * 
	 * @throws Exception
	 * @author shill
	 */
	@Test
	public void correctLoginTest() throws Exception {
		// prob wont work but i hope?
		String username = "user";
		when(userDaoMock.findByUsername(username)).thenReturn(FIRST_USER);

		MockHttpServletRequest req = mockMvc
				.perform(
						post("/j_spring_security_check").param("j_username",
								username).param("j_password", "password"))
				.andExpect(status().is(HttpStatus.FOUND.value()))
				.andExpect(redirectedUrl("/selectexperiment")).andReturn().getRequest();

		HttpSession session = req.getSession();
		// maybe forwarded idk
		Assert.assertNotNull(session);
		Assert.assertNull(req.getUserPrincipal());

	}

	/**
	 * Tests user login with invalid credentials. Expects to be returned to the
	 * same page with the login message. User should not be authentic.
	 * 
	 * @throws Exception
	 * @author shill
	 */
	@Test
	public void badPasswordLoginTest() throws Exception {
		// prob wont work but i hope?
		String username = "user";
		when(userDaoMock.findByUsername(username)).thenReturn(FIRST_USER);

		MockHttpServletRequest req = mockMvc
				.perform(
						post("/j_spring_security_check").param("j_username",
								username).param("j_password", "passsword"))
				.andExpect(status().is(HttpStatus.FOUND.value()))
				.andExpect(redirectedUrl("/auth/login?error=true")).andReturn()
				.getRequest();
		Assert.assertNull(req.getUserPrincipal());
	}

	/**
	 * User logs in with bad credentials (Creates session), and then attempts to
	 * navigate to the userarea. The app should redirect them to the login page.
	 * 
	 * @throws Exception
	 * @author shill
	 */
	@Test
	public void badLoginUserAreaTest() throws Exception {
		String username = "user";
		when(userDaoMock.findByUsername(username)).thenReturn(FIRST_USER);

		MockHttpServletRequest req = mockMvc
				.perform(
						post("/j_spring_security_check").param("j_username",
								username).param("j_password", "passsword"))
				.andExpect(status().is(HttpStatus.FOUND.value()))
				.andExpect(redirectedUrl("/auth/login?error=true")).andReturn()
				.getRequest();
		Assert.assertNull(req.getUserPrincipal());
		MockHttpServletResponse response = mockMvc.perform(get("/userarea"))
				.andReturn().getResponse();
		System.out
				.println("NOTICE: Bug in MvcMock for badLoginUserAreaTest. Redirects to the actual localhost address from security filter. To solve this the assertion is different than in previous tests.");
		assertEquals("Redirected URL", "http://localhost/auth/login",
				response.getRedirectedUrl());
	}

}