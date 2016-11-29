package edu.sjsu.cmpe275.project.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author Onkar Ganjewar
 */
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UniversityIdValidator.class)
@Documented
public @interface ValidUnivId {

    String message() default "Invalid University ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
