package edu.sjsu.cmpe275.project.service;

import java.util.List; 

import edu.sjsu.cmpe275.project.model.UserProfile;

/**
 * @author Onkar Ganjewar
 */
public interface UserProfileService {

	UserProfile findById(int id);

	UserProfile findByType(String type);
	
	List<UserProfile> findAll();
	
	void saveProfile(UserProfile userProfile);
	
}
