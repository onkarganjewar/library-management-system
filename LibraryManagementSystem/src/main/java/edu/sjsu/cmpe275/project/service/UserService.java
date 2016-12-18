package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.VerificationToken;

/**
 * @author Onkar Ganjewar
 */
public interface UserService {

	public User findById(int id);

	public User findByEmail(String email);

	public void saveUser(User user);

	public void updateUser(User user);

	public List<User> findAllUsers();

	public void createVerificationTokenForUser(User user, String token);

	public User findByToken(String verificationToken);

	public VerificationToken getVerificationToken(String VerificationToken);

	public VerificationToken generateNewVerificationToken(String existingVerificationToken);

	public String validateVerificationToken(String token);
	
	public void generateFines(int userId, double fine);

}