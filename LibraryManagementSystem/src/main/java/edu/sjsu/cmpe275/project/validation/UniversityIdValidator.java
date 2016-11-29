package edu.sjsu.cmpe275.project.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Onkar Ganjewar
 */
public class UniversityIdValidator implements ConstraintValidator<ValidUnivId, String> {
	private Pattern pattern;
	private Matcher matcher;
	/**
	 * University Id pattern to accept exact six digits
	 */
	private static final String ID_PATTERN = "^(\\s*\\d{6}\\s*)(,\\s*\\d{6}\\s*)*,?\\s*$";

	@Override
	public void initialize(final ValidUnivId constraintAnnotation) {
	}

	@Override
	public boolean isValid(final String uId, final ConstraintValidatorContext context) {
		return (validateUnivId(uId));
	}

	private boolean validateUnivId(final String uId) {
		pattern = Pattern.compile(ID_PATTERN);
		matcher = pattern.matcher(uId);
		return matcher.matches();
	}
}
