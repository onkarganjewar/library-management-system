package edu.sjsu.cmpe275.project.service;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 */
public interface NotificationService {

	void sendCheckoutMail(User user, Checkout checkout);
	void sendReturnMail (User user, Checkout checkout);
	void sendRegistrationConfirmationMail (User user, String applicationUrl);
	void sendRegistrationCompleteMail (User user, String applicationUrl);
}
