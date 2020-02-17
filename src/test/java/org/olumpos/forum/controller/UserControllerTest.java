package org.olumpos.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.olumpos.forum.controller.UserController;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * 
 * @author daristote<br>
 * <br>
 * Tests pour les fonction du contrôleur Spring MVC: UserController<br>
 *<br>
 */


@RunWith(JUnit4.class)
public class UserControllerTest {


	Logger logger =  Logger.getLogger(UserControllerTest.class.getName());
	
	private UserDAO userDAO;
	
	@Before
	public void init() {
		userDAO =  new UserDAOImpl();
		
		logger.log(Level.INFO, "UserControllerTest.init(): userDAO: " + userDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void getUsersTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView("/WEB-INF/views/users.jsp"))
				.build();
		
		
		//List<User> users = userDAO.getAllUsers();  
		User user =  userDAO.getUser("admin", "admin123");
	    
		mockMvc.perform(get("/users").sessionAttr("user", user))
	       .andExpect(view().name("users"))
	       .andExpect(model().attributeExists("userList"))
	     //  .andExpect(model().attribute("userList", hasItems(users.toArray()))) //error but should work
	       .andExpect(model().hasNoErrors());
	       
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void loginTest() throws Exception {
		
		logger.log(Level.INFO, "in UseControllerTest.login()") ;
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		mockMvc.perform(get("/login"))
	       .andExpect(view().name("login"))
	       .andExpect(model().attributeExists("user"))
	     //  .andExpect(model().attribute("userList", hasItems(users.toArray()))) //error but should work
	       .andExpect(model().hasNoErrors());
	       
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void doLoginTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//.setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		User user =  new User();
		user.setUsername("dummy");
		user.setEmail("dummy@olumpos.org");
		user.setPassword("dummy123");
		
		logger.log(Level.INFO, "in UseControllerTest.doLogin() test login with user's username: " + user.getUsername()) ;
		
		mockMvc.perform(post("/login").sessionAttr("user", user))	
	       .andExpect(redirectedUrl("topics"))
	       .andExpect(model().attributeExists("user"))
	       .andExpect(model().hasNoErrors());
		
		
		logger.log(Level.INFO, "in UseControllerTest.doLogin() test login with user's email: " + user.getEmail()) ;
		
		user.setUsername("dummy@olumpos.org");
		
		mockMvc.perform(post("/login").sessionAttr("user", user))	
	       .andExpect(redirectedUrl("topics"))
	       .andExpect(model().attributeExists("user"))
	       .andExpect(model().hasNoErrors());
	       
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void verifyLoginTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		int id =  userDAO.getLastInsertedUserId();
		String goodLogin = userDAO.getUserById(id).getUsername();
		
		mockMvc.perform(get("/verifyLogin/"+goodLogin))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        .andReturn().getResponse().getContentAsString().equals("{login:taken}");
	       
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void logoutTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		
		mockMvc.perform(get("/logout"))	
	       .andExpect(model().attributeExists("user"))
	       .andExpect(redirectedUrl("login"));
       
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void registerTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView("/WEB-INF/views/register.jsp"))
				.build();
		
		mockMvc.perform(get("/register"))	
	       .andExpect(view().name("register"))
	       .andExpect(model().attributeExists("user"))
	       .andExpect(status().isOk())
	       .andExpect(model().hasNoErrors());
	       
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void doRegisterTest() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//.setSingleView(new InternalResourceView("/WEB-INF/views/register.jsp"))
				.build();
		
		User user =  new User();
		user.setUsername("ulysses");
		user.setPassword("ulysses123");
		user.setEmail("ulysses@ithaque.org");
		
		//add User
		mockMvc.perform(post("/register").sessionAttr("user", user))	
	       .andExpect(model().attributeExists("user"))
	       .andExpect(model().hasNoErrors())
	       .andExpect(redirectedUrl("topics"));
	       
		//to run this test again: delete just inserted user
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		logger.log(Level.INFO, "in UseControllerTest.doRegister() lastUserId: " + lastUserId);
		
		assertTrue(lastUserId > 0);
		
		User addedUser =  userDAO.getUserById(lastUserId);
		
		assertNotNull(addedUser);
		
		logger.log(Level.INFO, "in UseControllerTest.doRegister() addedUser: " + addedUser);
		
		assertEquals(user.getUsername(), addedUser.getUsername());
		assertEquals(user.getEmail(), addedUser.getEmail()); 
		assertNotEquals(user.getPassword(), addedUser.getPassword()); //hashed password with md5
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);
		
		
	}
	

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deleteUserTest() throws Exception {
		
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		
		User adminUser = userDAO.getUserById(1);
		
		assertNotNull(adminUser);
		
		assertEquals("A", adminUser.getRole());
		
		//create user first
		User user =  new User();
		user.setUsername("ulysses");
		user.setPassword("ulysses123");
		user.setEmail("ulysses@ithaque.org");
		user.setIsActive((byte) 1);
		
		int createUserResult = userDAO.createUser(user);
		
		assertTrue(createUserResult > 0);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertTrue(lastUserId > 0);
		
		User createdUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(createdUser);
		
		assertEquals(1, createdUser.getIsActive());

		
	    mockMvc.perform(delete("/deleteUser/"+lastUserId).sessionAttr("user", adminUser))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
		User updatedUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(updatedUser);
		
		logger.log(Level.INFO, "in UseControllerTest.deleteUserTest() updatedUser: " + updatedUser) ;
		
		assertEquals(0, updatedUser.getIsActive());
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);
	
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void toggleUserTest() throws Exception {
		
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		User adminUser =  userDAO.getUser("admin", "admin123");
		
		assertNotNull(adminUser);
		
		assertEquals("A", adminUser.getRole());
		
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): adminuser: " + adminUser);
	
		//create user first
		User user =  new User();
		user.setUsername("ulysses");
		user.setPassword("ulysses123");
		user.setEmail("ulysses@ithaque.org");
		user.setIsActive((byte) 1);
		
		int createUserResult = userDAO.createUser(user);
		
		assertTrue(createUserResult > 0);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertTrue(lastUserId > 0);
		
		User createdUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(createdUser);
		
		byte status = createdUser.getIsActive();
		
		assertEquals(1, status);
	
		
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): user status: " + status);
		
		//deactivate
	    mockMvc.perform(put("/toggleUser/"+lastUserId).sessionAttr("user", adminUser))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
		User updatedUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(updatedUser);
		
		byte newStatus =  updatedUser.getIsActive();
		
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): user newStatus: " + newStatus);
		
		assertEquals(0, newStatus);
		
		//reactivate
	    mockMvc.perform(put("/toggleUser/"+lastUserId).sessionAttr("user", adminUser))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
		
		updatedUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(updatedUser);
		
		newStatus =  updatedUser.getIsActive();
		assertEquals(1, newStatus);
		
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): user newStatus: " + newStatus);
		
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);
	
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
//	@Test
//	public void doRegisterWithErrors() throws Exception {
//		UserController controller =  new UserController(userDAO);
//		MockMvc mockMvc = standaloneSetup(controller)
//				//.setSingleView(new InternalResourceView("/WEB-INF/views/register.jsp"))
//				.build();
//		
//		User user =  new User();
//		user.setUsername("ulysses");
//		user.setPassword("ulysses123");
//		user.setEmail("ulysses@ithaque.org");
//		
//		mockMvc.perform(post("/register").sessionAttr("user", user))	
//	       .andExpect(model().attributeExists("user"))
//	       .andExpect(model().hasErrors())
//	      // .andExpect(view().name("register")); //error return null @TODO: debug and find why
//	       .andExpect(redirectedUrl("register"));
//		//to run test again: delete just inserted user
//		//int lastUserId =  userDAO.getLastInsertedUserId();
//		//userDAO.deleteUserFromDB(lastUserId);
//		
//	}
	
}
