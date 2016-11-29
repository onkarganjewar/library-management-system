package edu.sjsu.cmpe275.project.service;

import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.UserProfileDao;
import edu.sjsu.cmpe275.project.model.UserProfile;

/**
 * @author Onkar Ganjewar
 */
@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService{
	
	@Autowired
	UserProfileDao dao;
	
	public UserProfile findById(int id) {
		return dao.findById(id);
	}

	public UserProfile findByType(String type){
		return dao.findByType(type);
	}

	public List<UserProfile> findAll() {
		return dao.findAll();
	}

	@Override
	public void saveProfile(UserProfile userProfile) {
		dao.save(userProfile);
	}
}
