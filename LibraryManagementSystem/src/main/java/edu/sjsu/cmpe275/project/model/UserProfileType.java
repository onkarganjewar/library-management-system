package edu.sjsu.cmpe275.project.model;

import java.io.Serializable; 

/**
 * @author Onkar Ganjewar
 */
public enum UserProfileType implements Serializable{
	USER("PATRON"),
	ADMIN("LIBRARIAN");
	
	String userProfileType;
	
	private UserProfileType(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
