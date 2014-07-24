package com.ddpsc.phenofront;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/testContext.xml", "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@WebAppConfiguration
public class DownloadManagerSuiteTest extends TestUtility {
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webappContext;
	@Before
	public void setUp() {
		//setup utility fields
		this.mockMvc = webAppContextSetup(this.webappContext).build();
		UserTestUtility();
	}
	
	//tests for download manager, system(downloadKey) => user || null
	/**
	 * Test for generating a new key
	 */
	@Test
	public void generateDownloadKeyTest(){
		String key = DownloadManager.generateRandomKey(FIRST_USER);
		String key2 = DownloadManager.generateRandomKey(SECOND_USER);

		Assert.assertEquals(FIRST_USER.getUsername(), System.getProperty(key));
		Assert.assertEquals(false, FIRST_USER.getUsername().equals(System.getProperty(key2)));
	}
	
	/**
	 * Test trying to generate a new key when one exists
	 * Current implementation supports multiple download keys
	 */
	@Test
	public void generateDownloadKeyAlreadyExistsTest(){
		//key=>user
		String key = DownloadManager.generateRandomKey(FIRST_USER);
		String key2 = DownloadManager.generateRandomKey(FIRST_USER);
		Assert.assertEquals(true, FIRST_USER.getUsername().equals(System.getProperty(key)));
		Assert.assertEquals(true, FIRST_USER.getUsername().equals(System.getProperty(key2)));

	}
	
	
	/**
	 * Test adding, removing, and adding a new download key.
	 */
	@Test
	public void generateRemoveGenerateDownloadKeyTest(){
		String key = DownloadManager.generateRandomKey(FIRST_USER);
		String key2 = DownloadManager.generateRandomKey(FIRST_USER);
		Assert.assertEquals(true, FIRST_USER.getUsername().equals(System.getProperty(key)));
		Assert.assertEquals(true, FIRST_USER.getUsername().equals(System.getProperty(key2)));
		
	}

	/**
	 * downloadkey does not exist on server 400 invalid key
	 * @throws Exception 
	 */
	@Test
	public void keyDoesNotExistTest() throws Exception{
        mockMvc.perform(get("/massdownload").param("downloadKey", "asdf")).andExpect(status().isBadRequest());		
	}
	
	
	

}
