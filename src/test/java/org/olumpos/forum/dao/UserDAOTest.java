package org.olumpos.forum.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.User;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction d'accès à la base de données de UserDAO utilisant JPA: 
 *
 */

//@RunWith(SpringRunner.class)
public class UserDAOTest {

	Logger logger =  Logger.getLogger(UserDAOTest.class.getName());
	

	private UserDAO userDAO;

	
	@Before
	public void init() {

		userDAO = new UserDAOImpl();
		logger.log(Level.INFO, "in UserDAOTest.init() ===========>  userDAO: " + userDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testGet() {
		
		logger.log(Level.INFO, "in UserDAOTest.testGet()");

		int userId =  userDAO.getLastInsertedUserId();
		
		//get user by id
		User user =  userDAO.getUserById(userId);
		assertNotNull(user);
		
		String username =  user.getUsername();
		String email =  user.getEmail();
		
	//	logger.log(Level.INFO, "in UserDAOTest.testGet() ===========>  username: " + username);
	//	logger.log(Level.INFO, "in UserDAOTest.testGet() ===========>  email: " + email);

		//get user by username
		user =  userDAO.getUserByUsername(username);
		assertNotNull(user);
		
		assertEquals(new Integer(userId), user.getId());
		assertEquals(email, user.getEmail());
		
		//get user by email
		user =  userDAO.getUserByUsername(email);
		assertNotNull(user);
		
		assertEquals(new Integer(userId), user.getId());
		assertEquals(username, user.getUsername());		
		
		
		////test login with username and password

		user =  userDAO.getUserByUsername("dummy");
		
		assertNotNull(user);
		
		String dummyUsername =  "dummy";
		String dummyPassword =  "dummy123";
		String dummyEmail = "dummy@olumpos.org";
		
		User dummyUser =  userDAO.getUser(dummyUsername, dummyPassword);
		assertNotNull(dummyUser);
		
		assertEquals(dummyUser.getId(), user.getId());
		
		//test login with email and password
		user =  userDAO.getUser(dummyEmail, dummyPassword);
		
		assertNotNull(user);
		
		assertEquals(dummyUser.getId(), user.getId());
		
		//logger.log(Level.INFO, "in UserDAOTest.testGet() ===========> getActiveUser with  username: " + dummyUsername);
		
		//test active user
		dummyUser =  userDAO.getActiveUser(dummyUsername, dummyPassword);
		assertNotNull(dummyUser);
		
		assertEquals(dummyUser.getId(), user.getId());
		
		//logger.log(Level.INFO, "in UserDAOTest.testGet() ===========> getActiveUser with email: " + dummyEmail);
		//test login with email and password
		dummyUser =  userDAO.getActiveUser(dummyEmail, dummyPassword);
		
		assertNotNull(user);
		
		assertEquals(dummyUser.getId(), user.getId());	
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testGetUserList() {

		
		logger.log(Level.INFO, "in UserDAOTest.testGetUserList() ");
		
		//get all users
		List<User> users =  userDAO.getAllUsers();
		assertNotNull(users);
		
		int nbUsers =  users.size();
		
	//	logger.log(Level.INFO, "in UserDAOTest.testGetUserList() nbUsers: " + nbUsers);
		//get all active users
		
		List<User> activeUsers =  userDAO.getAllActiveUsers();
		assertNotNull(users);
		
		int nbActiveUsers =  activeUsers.size();
		
		//logger.log(Level.INFO, "in UserDAOTest.testGetUserList() nbActiveUsers: " + nbActiveUsers);
		
		assertTrue(nbActiveUsers <= nbUsers);
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testCreateUpdateUser() {

		
		logger.log(Level.INFO, "in UserDAO Test.testCreateUpdateUser() ");

		User newUser = new User();
		newUser.setUsername("heracles");
		newUser.setEmail("heracles@olympus.org");
		newUser.setPassword("heracles123");
		
		int result = userDAO.createUser(newUser);
		
		assertEquals(1, result);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertNotEquals(0, result);
		
		User user =  userDAO.getUser(newUser.getUsername(), newUser.getPassword());
		
		assertNotNull(user);
		
		assertEquals(Integer.valueOf(lastUserId), user.getId());
		assertEquals(newUser.getUsername(), user.getUsername());
		assertEquals(newUser.getEmail(), user.getEmail());
		assertEquals(Integer.valueOf(lastUserId), user.getId());
			
		
		//logger.log(Level.INFO, "in UserDAOTest.testUpdateUser() ");
		
		//update username, email and password (mandatory) 
		String newUsername = "hercule";
		String newEmail = "hercule@olympus.org";
		String newPassword = "hercule123";

		
		user.setUsername(newUsername);
		user.setEmail(newEmail);
		user.setPassword(newPassword);
				
		result =  userDAO.updateUser(user);
		
		assertEquals(1, result);
		
		user =  userDAO.getUserById(user.getId());
		
		assertNotNull(user);
		
		assertEquals(newUsername, user.getUsername());
		assertEquals(newEmail, user.getEmail());
		
		newUsername = "herculus";
		newEmail = "herculus@olympus.org";
		newPassword = "herculus123";
		
		result = userDAO.updateUser(user.getId(), newUsername, newEmail, newPassword);
		
		user =  userDAO.getUserById(user.getId());
		
		assertNotNull(user);
		
		assertEquals(newUsername, user.getUsername());
		assertEquals(newEmail, user.getEmail());
		
		//for retesting
		userDAO.deleteUserFromDB(user.getId());
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testDeactivateUser() {
		logger.log(Level.INFO, "in UserDAOTest.testDeactivateUser() ");
		
		//deactivate user
		int lastUserId =  userDAO.getLastInsertedUserId();

		User user =  userDAO.getUserById(lastUserId);
		
		//make sure user is activated
		int result =  userDAO.activateDeactivateUser(user.getId(), (byte)1);
		assertEquals(1, result);
		
		//logger.log(Level.INFO, "in UserDAOTest.testUpdateUser() activate result " + result);
		
		//get all users
		List<User> allUsers =  userDAO.getAllUsers();
		assertNotNull(allUsers);
		
		int nbUsers =  allUsers.size();
		assertNotEquals(0, nbUsers);

		
		//get only active users
		List<User> activeUsers =  userDAO.getAllActiveUsers();
		
		assertNotNull(activeUsers);
		
		int nbActiveUsers =  activeUsers.size();

		assertTrue(nbActiveUsers <= nbUsers);
		
		
		result =  userDAO.activateDeactivateUser(user.getId(), (byte)0);
		assertEquals(1, result);
	
		activeUsers =  userDAO.getAllActiveUsers();
		assertNotNull(activeUsers);
		assertEquals(nbActiveUsers - 1, activeUsers.size());
		
		allUsers =  userDAO.getAllUsers();
		assertNotNull(allUsers);
		assertEquals(nbUsers, allUsers.size());
		
		
		//reactivate user
		
		user =  userDAO.getUserById(lastUserId);
		
		result =  userDAO.activateDeactivateUser(user.getId(), (byte)1);
		assertEquals(1, result);
		
		activeUsers =  userDAO.getAllActiveUsers();
		assertNotNull(activeUsers);
		assertEquals(nbActiveUsers, activeUsers.size());
		
		allUsers =  userDAO.getAllUsers();
		assertNotNull(allUsers);
		assertEquals(nbUsers, allUsers.size());
		
		
		

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	//
	@Test
	public void testErrors() {

		logger.log(Level.INFO, "in UserDAOTest.testErrors() ");
		
		//Get user with non valid id
		User user =  userDAO.getUserById(0);
		assertNull(user);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() wrong user id: " + user);
		
		//get user with bad username
		user =  userDAO.getUserByUsername("invalidUser");
		assertNull(user);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() wrong username " + user);
		
		
		//get user with wrong username or password
		user = userDAO.getUser("dummy", "dummy");
		assertNull(user);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() get wrong username or password: " + user);
		
		
		//update with non valid user id
		int result  =  userDAO.updateUser(0, "dummy", "email", "password");
		
		assertEquals(0, result);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() update with wrong user id. result:  " + result);
		
		
		//create with null user , username or email
		result = userDAO.createUser(null);
		assertEquals(0, result);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() create with null user ; result : " + result);

		
		result = userDAO.createUser(null, "email", "password");
		assertEquals(0, result);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() create with null username ; result:  " + result);
		
		result = userDAO.createUser("username", null, "password");
		assertEquals(0, result);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() create with null email ; result:  " + result);
		
		
		//delete with wrong user id
		result = userDAO.activateDeactivateUser(0, (byte)0);
		assertEquals(0, result);
		//logger.log(Level.INFO, "in UserDAOTest.testErrors() delete with bad user id ; result:  " + result);
				
	
	}
	
}
