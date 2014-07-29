package com.ddpsc.phenofront;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import src.ddpsc.database.user.DbGroup;
import src.ddpsc.database.user.DbUser;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.ObjectNotFoundException;


/**
 * This classes purpose is to test the user mangaement area of the admin section. It is impotant to note that NO security testing is
 * done in this class. All it does is test that the correct fields are sent to the server through the different POST requests.
 * 
 * @author shill, cjmcentee
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/testContext.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" })
@WebAppConfiguration
public class UserManagementSuiteTest extends TestUtility
{
    private MockMvc mockMvc;

    @Autowired
    private UserDao userDaoMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        // We have to reset our mock between tests because the mock objects
        // are managed by the Spring container. If we would not reset them,
        // stubbing and verified behavior would "leak" from one test to another.
        Mockito.reset(userDaoMock);
        UserTestUtility();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    /**
     * Tests the rendering of the base user management page. Expects a set of users.
     * 
     * @throws Exception
     */
    @Test
    public void userManagementBaseTest() throws Exception
    {
    	//TestUtility
        when(userDaoMock.findAllUsers()).thenReturn(USERS);

        mockMvc.perform(get("/admin/users"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("users"))
	        .andExpect(forwardedUrl("/WEB-INF/views/users.jsp"))
	        .andExpect(model().attribute("users", hasSize(3)))
	        .andExpect(model().attribute("users", hasItem(
	                allOf(hasProperty("userId", equalTo(1)),
	                      hasProperty("username", equalTo("testuser1"))))));
        
		verify(userDaoMock, times(1)).findAllUsers();
		verify(userDaoMock, times(1)).findAllGroups();
		verifyNoMoreInteractions(userDaoMock);
    }
    /**
     * Tests the changeusername post request. A username that does not exist is passed, expects success.
     * @author shill
     * @throws Exception
     */
    @Test
    public void changeUsernameSuccessTest() throws Exception
    {
    	String newValidUsername = "usernameNoOneElseHas";
    	int userId_secondUser = SECOND_USER.getUserId();
    	
    	doAnswer(new Answer<Object>() {
    		public Object answer(InvocationOnMock invocation) {
    			Object[] args = invocation.getArguments();
    			
    			DbUser user = (DbUser) args[0];
    			String newUsername = (String) args[1];
    			
    			user.setUsername(newUsername);
    			return null;
    		}
    	}).when(userDaoMock).changeUsername(SECOND_USER, newValidUsername);
        when(userDaoMock.findAllUsers()).thenReturn(USERS);
        when(userDaoMock.findByID(userId_secondUser)).thenReturn(SECOND_USER);
        
    	mockMvc.perform(post("/admin/changeusername")
    			.param("username", newValidUsername)
    			.param("userid", Integer.toString(userId_secondUser)))
    			.andExpect(status().isOk());
    	
    	Assert.assertThat(SECOND_USER.getUsername(), equalTo(newValidUsername));
    }
    
    /**
     * Tests the same request, but in a case where the chosen username already exists.
     * @throws Exception
     */
    @Test
    public void changeUsernameNotUniqueTest() throws Exception
    {
    	String username_firstUser = FIRST_USER.getUsername();
        int userId_secondUser = SECOND_USER.getUserId();
    	
        when(userDaoMock.findAllUsers()).thenReturn(USERS);
        when(userDaoMock.findByID(userId_secondUser)).thenReturn(SECOND_USER);
        when(userDaoMock.usernameExists(username_firstUser)).thenReturn(true);
        
        mockMvc.perform(post("/admin/changeusername")
        		.param("username", username_firstUser)
        		.param("userid", Integer.toString(userId_secondUser)))
			   .andExpect(status().isConflict());
        
        Assert.assertThat(SECOND_USER.getUsername(), not(equalTo(username_firstUser)));
    }
    /**
     * Tests the changeAuthority POST method with valid parameters. Expects success.
     * @throws Exception
     */
    @Test
    public void changeAuthoritySuccessTest() throws Exception
    {
    	String newValidAuthority = "ROLE_USER";
    	int userId_firstUser = FIRST_USER.getUserId();
    	
    	doAnswer(new Answer<Object>() {
    		public Object answer(InvocationOnMock invocation) {
    			Object[] args = invocation.getArguments();
    			
    			DbUser user = (DbUser) args[0];
    			String newAuthority = (String) args[1];
    			
    			user.setAuthority(newAuthority);
    			return null;
    		}
    	}).when(userDaoMock).changeAuthority(FIRST_USER, newValidAuthority);
		when(userDaoMock.findByID(userId_firstUser)).thenReturn(FIRST_USER); 
    	
    	
    	mockMvc.perform(post("/admin/changeauthority")
    			.param("authority", newValidAuthority)
    			.param("userid", Integer.toString(userId_firstUser)))
			.andExpect(status().isOk());
    	
    	Assert.assertThat(FIRST_USER.getAuthority(), equalTo(newValidAuthority));
    }
    /**
     * Tests the changeAuthority method with an invalid authority. Expects a badRequest response.
     * @throws Exception
     */
    @Test
    public void changeAuthorityInvalidTest() throws Exception
    {
    	String newInvalidAuthority = "ROLE_INVALID_AUTHORITY"; 
    	int userId_firstUser = FIRST_USER.getUserId(); 
    	
    	when(userDaoMock.findByID(userId_firstUser)).thenReturn(FIRST_USER);
    	
    	mockMvc.perform(post("/admin/changeauthority")
    			.param("authority", newInvalidAuthority)
    			.param("userid", Integer.toString(userId_firstUser)))
			.andExpect(status().isBadRequest());
    	
    	Assert.assertThat(FIRST_USER.getAuthority(), not(equalTo(newInvalidAuthority))); 
    }
    
    /**
     * Tests the post request for changing a user's group. Must be a valid group. Does not check userId, since that should never
     * happen anyway. (limited by forms)
     */
    @Test
    public void changeGroupSuccessTest() throws Exception
    {
    	String groupName_secondUser = SECOND_USER_GROUP.getGroupName();
    	int userId_firstUser = FIRST_USER.getUserId();
    	
    	doAnswer(new Answer<Object>() {
    		public Object answer(InvocationOnMock invocation) {
    			Object[] args = invocation.getArguments();
    			DbUser user = (DbUser) args[0];
    			DbGroup newGroup = (DbGroup) args[1];
    			user.setGroup(newGroup);
    			return null;
    		}
    	}).when(userDaoMock).changeGroup(FIRST_USER, SECOND_USER_GROUP);
    	when(userDaoMock.findByID(userId_firstUser)).thenReturn(FIRST_USER);
    	when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	when(userDaoMock.findGroupByName(groupName_secondUser)).thenReturn(SECOND_USER_GROUP);
    	
    	mockMvc.perform(post("/admin/changegroup")
    			.param("groupname", groupName_secondUser)
    			.param("userid", Integer.toString(userId_firstUser)))
			.andExpect(status().isOk());
    	
    	Assert.assertThat(FIRST_USER.getGroup(), equalTo(SECOND_USER_GROUP));
    }
    
    /**
     * Tests for the instance where a bad group name is passed. This shouldn't happen but it is explicitely checked in the
     * controller so it is checked here.
     * 
     * @throws Exception
     */
    @Test
    public void changeGroupNoSuchGroupTest() throws Exception
    {
    	int userId_firstUser = FIRST_USER.getUserId();
    	String nonExistentGroupName = "Phoenix Suns";
    	
    	Mockito.doThrow(new ObjectNotFoundException()).when(userDaoMock).findGroupByName(nonExistentGroupName);
    	when(userDaoMock.findByID(userId_firstUser)).thenReturn(FIRST_USER);
    	when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	
    	mockMvc.perform(post("/admin/changegroup")
    			.param("groupname", nonExistentGroupName)
    			.param("userid", Integer.toString(userId_firstUser)))
    		.andExpect(status().isBadRequest());

    	Assert.assertThat(FIRST_USER.getGroup().getGroupName(), not(equalTo(nonExistentGroupName)));    	
    }
    
    /**
     * Tests the post instance of deleting a user. We cannot check to see if an object is removed becuase they are removed 
     * from the database, instead just check that the status is OK (200)
     * @throws Exception
     */
    @Test
    public void deleteUserSuccessTest() throws Exception
    {
    	int userId_firstUser = FIRST_USER.getUserId();
    	
    	when(userDaoMock.findByID(userId_firstUser)).thenReturn(FIRST_USER); 
    	
    	mockMvc.perform(post("/admin/removeuser")
    			.param("userid", Integer.toString(userId_firstUser)))
    		.andExpect(status().isOk());
    }
    
    /**
     * A redundant, UserNotFoundException is a compile-time exception, so it must be handled in the controller. It is the
     * only error checking that occurs for this action though, so it is checked.
     * @throws Exception
     */
    @Test
    public void deleteUserNoUserFoundTest() throws Exception
    {
    	int nonExistentUserId = -5;
    	
    	when(userDaoMock.findByID(-5)).thenThrow(new ObjectNotFoundException()); 
    	
    	mockMvc.perform(post("/admin/removeuser")
    			.param("userid", Integer.toString(nonExistentUserId)))
    		.andExpect(status().isBadRequest());
    }
    
    /**
     * Tests the the newuser post method with all valid parameters.
     * @throws Exception
     */
    @Test
    public void addNewUserSuccessTest() throws Exception
    {
    	String username = "newuser";
    	String password = "newpassword";
    	String validate = "newpassword";
    	String authority = "ROLE_USER";
    	String groupName = FIRST_USER_GROUP.getGroupName();
    	
    	when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	when(userDaoMock.findAllUsers()).thenReturn(USERS);
    	
    	mockMvc.perform(post("/admin/newuser")
    			.param("username",username)
    			.param("password", password)
    			.param("validate", validate)
    			.param("authority", authority)
    			.param("groupname", groupName))
    			.andExpect(status().isOk());
    }
    /**
     * Tests for error if username already exists.
     * @throws Exception
     */
    @Test
    public void addNewUserUsernameExistsTest() throws Exception
    {
    	String alreadyExistingUsername = FIRST_USER.getUsername();
		String password = "newpassword";
		String validate = "newpassword";
		String authority = "ROLE_USER";
		String groupName = FIRST_USER_GROUP.getGroupName();
    	
    	when(userDaoMock.usernameExists(alreadyExistingUsername)).thenReturn(true);
		when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
		when(userDaoMock.findAllUsers()).thenReturn(USERS);
		
		mockMvc.perform(post("/admin/newuser")
				.param("username", alreadyExistingUsername)
				.param("password", password)
				.param("validate", validate)
				.param("authority", authority)
				.param("groupname", groupName))
				.andExpect(status().isConflict());
    }
    
    /**
	 * Tests for error on bad authority input.
     * @throws Exception
     */
    @Test
    public void addNewUserInvalidAuthorityTest() throws Exception
    {
		String username = "newuser";
		String password = "newpassword";
		String validate = "newpassword";
		String invalidAuthority = "ROLE_INVALID_AUTHORITY";
		String groupName = FIRST_USER_GROUP.getGroupName();  
		
		when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
		when(userDaoMock.findAllUsers()).thenReturn(USERS);

		mockMvc.perform(post("/admin/newuser")
				.param("username",username)
				.param("password", password)
				.param("validate", validate)
				.param("authority", invalidAuthority)
				.param("groupname", groupName))
				.andExpect(status().isBadRequest());
    }
    
    /**
     * Tests for error on the password and validation not matching.
     * @throws Exception
     */
	@Test
	public void addNewUserNotPassMatchTest() throws Exception
	{
		String username = "newuser";
		String password = "newpassword";
		String nonMatchingValidate = "passwordThatDoesNotMatch";
		String authority = "ROLE_USER";
		String groupName = FIRST_USER_GROUP.getGroupName();
		
		when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
	   	when(userDaoMock.findAllUsers()).thenReturn(USERS);
	   	
		mockMvc.perform(post("/admin/newuser")
			.param("username",username)
			.param("password", password)
			.param("validate", nonMatchingValidate)
			.param("authority", authority)
			.param("groupname", groupName))
			.andExpect(status().isBadRequest());
	}
    


}