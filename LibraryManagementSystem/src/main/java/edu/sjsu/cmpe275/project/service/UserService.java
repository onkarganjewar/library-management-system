package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;

/**
 * @author Onkar Ganjewar
 */
public interface UserService {

	User findById(int id);

	User findByEmail(String email);

	void saveUser(User user);

	void updateUser(User user);

	List<User> findAllUsers();

	void createVerificationTokenForUser(User user, String token);

	User findByToken(String verificationToken);

	VerificationToken getVerificationToken(String VerificationToken);

	VerificationToken generateNewVerificationToken(String existingVerificationToken);

	String validateVerificationToken(String token);

}