package edu.sjsu.cmpe275.project.service;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */
public interface NotificationService {

	public void sendCheckoutMail(User user, Checkout checkout);
	public void sendReturnMail (User user, Checkout checkout);
	public void sendRegistrationConfirmationMail (User user, String applicationUrl);
	public void sendRegistrationCompleteMail (User user, String applicationUrl);
	public void sendDueDateAlertMail (Checkout checkout, Integer dueWithin);
	public void sendBookAvailableMail (Checkout checkout, Integer dueWithin);
}
