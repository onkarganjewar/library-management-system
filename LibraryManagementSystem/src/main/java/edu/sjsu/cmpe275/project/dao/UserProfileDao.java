
package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.UserProfile; 

/**
 * @author Onkar Ganjewar
 */
public interface UserProfileDao {

	List<UserProfile> findAll();
	
	UserProfile findByType(String type);
	
	UserProfile findById(int id);
	
	void save(UserProfile userProfile);

}
