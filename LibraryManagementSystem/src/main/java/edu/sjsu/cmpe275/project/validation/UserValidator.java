package edu.sjsu.cmpe275.project.validation;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.sjsu.cmpe275.project.dao.UserDao;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.UserProfile;
import edu.sjsu.cmpe275.project.service.UserProfileService;

/**
 * @author Onkar Ganjewar
 */
@Component
public class UserValidator implements Validator {

	@Autowired
	private UserDao userDAO;

	@Autowired
	UserProfileService userProfileService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		boolean duplicateUser = false;
		// Checks if the user with the same university Id tries to create both
		// patron and librarian account.
		User returnUser = userDAO.findByUnivId(user.getuId());
		if (returnUser != null) {
			System.out.println("DATABASE EXISTING USER = "+returnUser);
			Set<UserProfile> profileSet = new HashSet<UserProfile>();
			Set<UserProfile> returnUserProfiles = new HashSet<UserProfile>();
			returnUserProfiles = returnUser.getUserProfiles();
			// /** If the user owns an sjsu email id then he/she is automatically
			// gets a librarian account. */
			if (user.getEmail().contains("@sjsu.edu")) {
				UserProfile profile = new UserProfile();
				profile = userProfileService.findByType("ADMIN");
				profileSet.add(profile);
				user.setUserProfiles(profileSet);
			} else {
				UserProfile profile = new UserProfile();
				profile = userProfileService.findByType("USER");
				profileSet.add(profile);
				user.setUserProfiles(profileSet);
			}

			if (profileSet.equals(returnUserProfiles))
				duplicateUser = true;
			
			// Compare the two user profiles of the user
			// If the librarian tries to register for patron account then throw the
			// error.
			if (duplicateUser) {
				if (user.getEmail().contains("sjsu.edu"))
					errors.rejectValue("uId", "message.librarian");
				else
					errors.rejectValue("uId", "UniqueId.user.uId");
			}
//			if (!(profileSet.containsAll(returnUser.getUserProfiles())))
//				duplicateUser  = true;
//
//			if (returnUser.getEmail().equals(user.getEmail()) && duplicateUser)
//				errors.rejectValue("email", "message.librarian");
		}
		System.out.println("DONE");
	}
}