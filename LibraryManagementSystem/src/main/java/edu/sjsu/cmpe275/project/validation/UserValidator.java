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

/**
 * @author Onkar Ganjewar
 */
@Component
public class UserValidator implements Validator {

	@Autowired
	private UserDao userDAO;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		// /** If the user owns an sjsu email id then he/she is automatically
		// gets a librarian account. */
//		if (user.getEmail().contains("@sjsu.edu"))
//			errors.rejectValue("email", "message.email.sjsu");

		boolean duplicateUser = false;
		// Checks if the user with the same university Id tries to create both
		// patron and librarian account.
		User user2 = userDAO.findByUnivId(user.getuId());
		if (user2 != null) {
			System.out.println(user2);
			Set<UserProfile> profileSet = new HashSet<UserProfile>();
			profileSet = user.getUserProfiles();
			// If the librarian tries to register for patron account then throw the
			// error.
			if (!(profileSet.containsAll(user2.getUserProfiles())))
				duplicateUser  = true;

			if (user2.getEmail().equals(user.getEmail()) && duplicateUser)
				errors.rejectValue("email", "message.librarian");
		}
		System.out.println("DONE");
	}
}