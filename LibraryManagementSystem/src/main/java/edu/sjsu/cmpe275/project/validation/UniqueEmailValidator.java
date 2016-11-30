/**
 * 
 */
package edu.sjsu.cmpe275.project.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 * 
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserService userService;
	
	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(UniqueEmail constraintAnnotation) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return validateEmail(value);
	}

	/**
	 * @param value
	 * @return
	 */
	private boolean validateEmail(String value) {
		// TODO Auto-generated method stub
		User user = userService.findByEmail(value);
		if (user == null)
			return true;
		return false;
	}
}
