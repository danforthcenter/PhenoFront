package com.ddpsc.phenofront;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import src.ddpsc.database.user.DbGroup;
import src.ddpsc.database.user.UserDao;
import src.ddpsc.exceptions.UserNotFoundException;


/**
 * This classes purpose is to test the user mangaement area of the admin section. It is impotant to note that NO security testing is
 * done in this class. All it does is test that the correct fields are sent to the server through the different POST requests.
 * 
 * @author shill
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/testContext.xml", "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@WebAppConfiguration
public class UserManagementTestSuite extends TestUtility{

    private MockMvc mockMvc;

    @Autowired
    private UserDao userDaoMock;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        //We have to reset our mock between tests because the mock objects
        //are managed by the Spring container. If we would not reset them,
        //stubbing and verified behavior would "leak" from one test to another.
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
    public void userManagementBaseTest() throws Exception {
      
    	//TestUtility
        when(userDaoMock.findAllUsers()).thenReturn(USERS);

        mockMvc.perform(get("/admin/users"))
        .andExpect(status().isOk())
        .andExpect(view().name("users"))
        .andExpect(forwardedUrl("/WEB-INF/views/users.jsp"))
        .andExpect(model().attribute("users", hasSize(3)))
        .andExpect(model().attribute("users", hasItem(
                allOf(
                        hasProperty("userId", is(1)),
                        hasProperty("username", is("testuser1"))
                )
        )));
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
    public void changeUsernameSuccessTest() throws Exception {
        when(userDaoMock.findAllUsers()).thenReturn(USERS);
        when(userDaoMock.findByUserId(2)).thenReturn(SECOND_USER); 

        String username = "uniqueusername";
        String userid = Integer.toString(SECOND_USER.getUserId());
    	mockMvc.perform(post("/admin/changeusername").param("username", username).param("userid", userid))
    		.andExpect(status().isOk());
    	Assert.assertEquals(SECOND_USER.getUsername(),username); //should actually change
    	
    }
    
    /**
     * Tests the same request, but in a case where the chosen username already exists.
     * @throws Exception
     */
    @Test
    public void changeUsernameNotUniqueTest() throws Exception {
        when(userDaoMock.findAllUsers()).thenReturn(USERS); 
        when(userDaoMock.findByUserId(2)).thenReturn(SECOND_USER); 
        String username = FIRST_USER.getUsername();
        String orig = SECOND_USER.getUsername(); //for later check
        String userid = Integer.toString(SECOND_USER.getUserId());
        mockMvc.perform(post("/admin/changeusername").param("username", username).param("userid", userid))
			.andExpect(status().isConflict());
        Assert.assertEquals(SECOND_USER.getUsername(), orig); //shouldn't have changed!
    }
    /**
     * Tests the changeAuthority POST method with valid parameters. Expects success.
     * @throws Exception
     */
    @Test
    public void changeAuthoritySuccessTest() throws Exception {
		when(userDaoMock.findByUserId(1)).thenReturn(FIRST_USER); 
    	String newAuth = "ROLE_USER";
    	String userId = Integer.toString( FIRST_USER.getUserId()); 
    	mockMvc.perform(post("/admin/changeauthority").param("authority", newAuth).param("userid", userId))
			.andExpect(status().isOk());
    	Assert.assertEquals(FIRST_USER.getAuthority(), newAuth); 
    }
    /**
     * Tests the changeAuthority method with an invalid authority. Expects a badRequest response.
     * @throws Exception
     */
    @Test
    public void changeAuthorityInvalidTest() throws Exception {
    	when(userDaoMock.findByUserId(1)).thenReturn(FIRST_USER); 
    	String newAuth = "ROLE_FAKEEEEER";
    	String orig = FIRST_USER.getAuthority(); 
    	String userId = Integer.toString( FIRST_USER.getUserId()); 
    	mockMvc.perform(post("/admin/changeauthority").param("authority", newAuth).param("userid", userId))
			.andExpect(status().isBadRequest());
    	Assert.assertEquals(FIRST_USER.getAuthority(), orig); 
    }
    
    /**
     * Tests the post request for changing a user's group. Must be a valid group. Does not check userId, since that should never
     * happen anyway. (limited by forms)
     */
    @Test
    public void changeGroupSuccessTest() throws Exception{
    	when(userDaoMock.findByUserId(1)).thenReturn(FIRST_USER); 
    	when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	String userId = Integer.toString( FIRST_USER.getUserId()); 
    	String newGroup = SECOND_USER_GROUP.getGroupName();
    	mockMvc.perform(post("/admin/changegroup").param("groupname", newGroup).param("userid", userId))
			.andExpect(status().isOk());
    	Assert.assertEquals(FIRST_USER.getGroup(), SECOND_USER_GROUP);     	
    }
    
    /**
     * Tests for the instance where a bad group name is passed. This shouldn't happen but it is explicitely checked in the
     * controller so it is checked here.
     * 
     * @throws Exception
     */
    @Test
    public void changeGroupNoSuchGroupTest() throws Exception{
    	when(userDaoMock.findByUserId(1)).thenReturn(FIRST_USER); 
    	when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	String userId = Integer.toString( FIRST_USER.getUserId()); 
    	String newGroup = "Phoenix Suns";
    	DbGroup origGroup = FIRST_USER.getGroup();
    	mockMvc.perform(post("/admin/changegroup").param("groupname", newGroup).param("userid", userId))
			.andExpect(status().isBadRequest());
    	Assert.assertEquals(FIRST_USER.getGroup(), origGroup);    	
    }
    
    /**
     * Tests the post instance of deleting a user. We cannot check to see if an object is removed becuase they are removed 
     * from the database, instead just check that the status is OK (200)
     * @throws Exception
     */
    @Test
    public void deleteUserSuccessTest() throws Exception {
    	when(userDaoMock.findByUserId(1)).thenReturn(FIRST_USER); 
    	String userId = Integer.toString( FIRST_USER.getUserId()); 
    	mockMvc.perform(post("/admin/removeuser").param("userid", userId))
    		.andExpect(status().isOk());

    }
    
    /**
     * A redundant, UserNotFoundException is a compile-time exception, so it must be handled in the controller. It is the
     * only error checking that occurs for this action though, so it is checked.
     * @throws Exception
     */
    @Test
    public void deleteUserNoUserFoundTest() throws Exception {
    	when(userDaoMock.findByUserId(-5)).thenThrow(new UserNotFoundException()); 
    	String userId = "-5";
    	mockMvc.perform(post("/admin/removeuser").param("userid", userId))
    		.andExpect(status().isBadRequest());
    }
    /**
     * Tests the the newuser post method with all valid parameters.
     * @throws Exception
     */
    @Test
    public void addNewUserSuccessTest() throws Exception {
		  when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	  when(userDaoMock.findAllUsers()).thenReturn(USERS);
    	  String username = "newuser";
    	  String password = "newpassword";
    	  String validate = "newpassword";
    	  String authority = "ROLE_USER";
    	  String groupName = FIRST_USER_GROUP.getGroupName();
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
    public void addNewUserUsernameExistsTest() throws Exception {
		  when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	  when(userDaoMock.findAllUsers()).thenReturn(USERS);
    	  String username = FIRST_USER.getUsername();
    	  String password = "newpassword";
    	  String validate = "newpassword";
    	  String authority = "ROLE_USER";
    	  String groupName = FIRST_USER_GROUP.getGroupName();
    	  mockMvc.perform(post("/admin/newuser")
    			  .param("username",username)
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
    public void addNewUserInvalidAuthorityTest() throws Exception {
		  when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
    	  when(userDaoMock.findAllUsers()).thenReturn(USERS);
    	  String username = "newuser";
    	  String password = "newpassword";
    	  String validate = "newpassword";
    	  String authority = "ROLE_THE_SISKO";
    	  String groupName = FIRST_USER_GROUP.getGroupName();
    	  mockMvc.perform(post("/admin/newuser")
    			  .param("username",username)
    			  .param("password", password)
    			  .param("validate", validate)
    			  .param("authority", authority)
    			  .param("groupname", groupName))
    			  .andExpect(status().isBadRequest());
    }
    
    /**
     * Tests for error on the password and validation not matching.
     * @throws Exception
     */
	@Test
	public void addNewUserNotPassMatchTest() throws Exception {
		when(userDaoMock.findAllGroups()).thenReturn(GROUPS);
	   	when(userDaoMock.findAllUsers()).thenReturn(USERS);
	   	String username = "newuser";
		String password = "newpassword";
		String validate = "newpassword1";
		String authority = "ROLE_USER";
		String groupName = FIRST_USER_GROUP.getGroupName();
		mockMvc.perform(post("/admin/newuser")
			.param("username",username)
			.param("password", password)
			.param("validate", validate)
			.param("authority", authority)
			.param("groupname", groupName))
			.andExpect(status().isBadRequest());
	}
    


}