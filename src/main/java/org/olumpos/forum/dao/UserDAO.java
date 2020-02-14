package org.olumpos.forum.dao;

import java.util.List;

import org.olumpos.forum.entity.User;

public interface UserDAO {

	public List<User> getAllUsers();
	public List<User> getAllActiveUsers();
	public User getUserById(int id);
	public User getUserByUsername(String username);
	public User getUser(String username, String password);
	public User getActiveUser(String username, String password);
	public int getLastInsertedUserId();
	public int createUser(User user);
	public int createUser(String username, String email, String password);
	public int updateUser(User user);
	public int updateUser(int id, String username, String email, String password);
	public int deleteUserFromDB(int userId);
	public int activateDeactivateUser(int userId, byte status);
	
}
