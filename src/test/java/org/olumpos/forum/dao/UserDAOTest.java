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
	public void getAllUsersTest() {
		
		
		logger.log(Level.INFO, "in UserDAOTest.getAllUsersTest() ");
		
		List<User> users = userDAO.getAllUsers();
		
		assertNotNull(users);
		
		int allUsersSize = users.size();
		
		assertTrue(allUsersSize > 0);
		
		List<User> activeUsers = userDAO.getAllActiveUsers();
		
		assertNotNull(activeUsers);
		
		int activeUsersSize = activeUsers.size();
		
		assertTrue(activeUsersSize > 0);
		
		assertTrue(activeUsersSize <= allUsersSize);
		
	}
	
	//*************************************************************************************************************************************************
	//*************************************************************************************************************************************************
	
	@Test
	public void getUserTest() {
		
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
		
		assertEquals(Integer.valueOf(userId), user.getId());
		assertEquals(username, user.getUsername());		
		
		
		////test login with username and password

		//add user first
		
		//create user first
		User newUser =  new User();
		newUser.setUsername("ulysses");
		newUser.setPassword("ulysses123");
		newUser.setEmail("ulysses@ithaque.org");
		newUser.setIsActive((byte) 1);
		
		int createUserResult = userDAO.createUser(newUser);
		
		assertTrue(createUserResult > 0);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertTrue(lastUserId > 0);
		
		User createdUser = userDAO.getUserById(lastUserId);
		
		assertNotNull(createdUser);
		
		
		User loggedUser =  userDAO.getUser(newUser.getUsername(), newUser.getPassword());
		assertNotNull(loggedUser);
		
		assertEquals(loggedUser.getId(), createdUser.getId());
		
		//test login with email and password
		loggedUser =  userDAO.getUser(newUser.getEmail(), newUser.getPassword());
		
		assertNotNull(loggedUser);
		
		assertEquals(createdUser.getId(), loggedUser.getId());
		
		//logger.log(Level.INFO, "in UserDAOTest.testGet() ===========> getActiveUser with  username: " + dummyUsername);
		
		//test active user with username and password
		loggedUser =  userDAO.getActiveUser(newUser.getUsername(), newUser.getPassword());
		assertNotNull(loggedUser);
		
		assertEquals(createdUser.getId(), loggedUser.getId());
		
		//logger.log(Level.INFO, "in UserDAOTest.testGet() ===========> getActiveUser with email: " + dummyEmail);
		//test login with email and password
		loggedUser =  userDAO.getActiveUser(newUser.getEmail(), newUser.getPassword());
		assertNotNull(loggedUser);
		
		assertEquals(createdUser.getId(), loggedUser.getId());
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);
		
	}
	

	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void updateUserTest() {

		
		logger.log(Level.INFO, "in UserDAO Test.testCreateUpdateUser() ");

		//create new user
		User newUser = new User();
		newUser.setUsername("heracles");
		newUser.setEmail("heracles@olympus.org");
		newUser.setPassword("heracles123");
		
		int result = userDAO.createUser(newUser);
		
		assertEquals(1, result);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertNotEquals(0, result);
		
		User createdUser =  userDAO.getUser(newUser.getUsername(), newUser.getPassword());
		
		assertNotNull(createdUser);
		
		assertEquals(Integer.valueOf(lastUserId), createdUser.getId());
		assertEquals(newUser.getUsername(), createdUser.getUsername());
		assertEquals(newUser.getEmail(), createdUser.getEmail());
			
		
		//logger.log(Level.INFO, "in UserDAOTest.testUpdateUser() ");
		
		//update username, email and password (mandatory) 
		String newUsername = "hercule";
		String newEmail = "hercule@olympus.org";
		String newPassword = "hercule123";

		
		createdUser.setUsername(newUsername);
		createdUser.setEmail(newEmail);
		createdUser.setPassword(newPassword);
				
		int updatedUserResult =  userDAO.updateUser(createdUser);
		
		assertEquals(1, updatedUserResult);
		
		User updatedUser =  userDAO.getUserById(lastUserId);
		
		assertNotNull(updatedUser);
		
		assertEquals(newUsername, updatedUser.getUsername());
		assertEquals(newEmail, updatedUser.getEmail());
	
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);
		
		
		
		
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void testDeactivateUser() {
		logger.log(Level.INFO, "in UserDAOTest.testDeactivateUser() ");
		
		//deactivate user
		
		//create new user
		User newUser = new User();
		newUser.setUsername("heracles");
		newUser.setEmail("heracles@olympus.org");
		newUser.setPassword("heracles123");
		newUser.setIsActive((byte) 1);
		
		int result = userDAO.createUser(newUser);
		
		assertEquals(1, result);
		
		int lastUserId =  userDAO.getLastInsertedUserId();
		
		assertNotEquals(0, result);
		
		User createdUser =  userDAO.getUser(newUser.getUsername(), newUser.getPassword());
		
		assertNotNull(createdUser);

		assertEquals(1, createdUser.getIsActive());
	
	
		//deactivate user
		
		int deactivatedUserResult =  userDAO.activateDeactivateUser(lastUserId, (byte)0);
		assertEquals(1, deactivatedUserResult);
	
		User deactivatedUser =  userDAO.getUserById(lastUserId);
		
		assertNotNull(deactivatedUser);
		assertEquals(0, deactivatedUser.getIsActive() );
		
		//reactivate user
		
		int activatedUserResult =  userDAO.activateDeactivateUser(lastUserId, (byte)1);
		assertEquals(1, activatedUserResult);
	
		User activatedUser =  userDAO.getUserById(lastUserId);
		
		assertNotNull(activatedUser);
		assertEquals(1, activatedUser.getIsActive() );
		
		
		//delete user from db
		int deletedUserResult = userDAO.deleteUserFromDB(lastUserId);
		
		assertTrue(deletedUserResult > 0);
		
		User deletedUser =  userDAO.getUserById(lastUserId);
		
		assertNull(deletedUser);

	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	//
	@Test
	public void errorsTest() {

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
