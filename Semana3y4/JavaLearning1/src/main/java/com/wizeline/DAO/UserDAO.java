/**
 * 
 */
package com.wizeline.DAO;

@interface
public interface UserDAO {

	String createUser(String user, String password);
	
	String login(String user, String password);
	
}
