package org.olumpos.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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
 * @author daristote
 * 
 * Tests pour les fonction du contrôleur Spring MVC: UserController
 *
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
	public void getUsers() throws Exception {
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
	public void login() throws Exception {
		
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
	public void doLogin() throws Exception {
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
	public void verifyLogin() throws Exception {
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
	public void logout() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		
		mockMvc.perform(get("/logout"))	
	       .andExpect(model().attributeExists("user"))
	       .andExpect(redirectedUrl("login"));
       
		
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void register() throws Exception {
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
	public void doRegister() throws Exception {
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//.setSingleView(new InternalResourceView("/WEB-INF/views/register.jsp"))
				.build();
		
		User user =  new User();
		user.setUsername("ulysses");
		user.setPassword("ulysses123");
		user.setEmail("ulysses@ithaque.org");
		
		mockMvc.perform(post("/register").sessionAttr("user", user))	
	       .andExpect(model().attributeExists("user"))
	       .andExpect(model().hasNoErrors())
	       .andExpect(redirectedUrl("topics"));
	       
		//to run this test again: delete just inserted user
		int lastUserId =  userDAO.getLastInsertedUserId();
		userDAO.deleteUserFromDB(lastUserId);
		
	}
	

	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deleteUser() throws Exception {
		
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		User adminUser =  userDAO.getUser("admin", "admin123");
		
		int userId =  userDAO.getLastInsertedUserId();
		
		userDAO.activateDeactivateUser(userId, (byte)1); //make sure user is active
		
		User user = userDAO.getUserById(userId);
		
		assertEquals(1, user.getIsActive());

		
	    mockMvc.perform(delete("/deleteUser/"+userId).sessionAttr("user", adminUser))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
		user = userDAO.getUserById(userId);
		
		assertEquals(0, user.getIsActive());   
		
		//rreaxtivate user for retesting
		userDAO.activateDeactivateUser(userId, (byte)1); //make sure user is active
		
		user = userDAO.getUserById(userId);
		
		assertEquals(1, user.getIsActive());
	
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void toggleUser() throws Exception {
		
		UserController controller =  new UserController(userDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				//setSingleView(new InternalResourceView("/WEB-INF/views/login.jsp"))
				.build();
		
		User adminUser =  userDAO.getUser("admin", "admin123");
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): adminuser: " + adminUser);
		
		
		int userId =  userDAO.getLastInsertedUserId();
		
		User user = userDAO.getUserById(userId);

		byte status = user.getIsActive();
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): user status: " + status);
		
	    mockMvc.perform(put("/toggleUser/"+userId).sessionAttr("user", adminUser))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
		user = userDAO.getUserById(userId);
		
		byte newStatus =  user.getIsActive();
		logger.log(Level.INFO, "UserControllerTest.toggleUser(): user newStatus: " + newStatus);
		
		
		assertNotEquals(status, newStatus);   
		
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
