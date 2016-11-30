/**
 * 
 */
package edu.sjsu.cmpe275.project.dao;


import java.util.List;

import edu.sjsu.cmpe275.project.model.User; 


/**
 * @author Onkar Ganjewar
 */
public interface UserDao {

	User findById(int id);

	User findByEmail(String email);
	
	void save(User user);
	
	List<User> findAllUsers();
	
	User findByUnivId(String getuId);
}

