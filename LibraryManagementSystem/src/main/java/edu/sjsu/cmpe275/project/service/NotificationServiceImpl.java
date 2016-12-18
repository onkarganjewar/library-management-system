package edu.sjsu.cmpe275.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.WaitList;
import edu.sjsu.cmpe275.project.util.CustomMailSender;

/**
 * @author Onkar Ganjewar
 */
@Service("notificationService")
@Transactional
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private CustomMailSender mailSender;

	@Override
	public void sendCheckoutMail(User user, Checkout checkout) {
		try {
			mailSender.sendMail(user, checkout, null, 2, null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendReturnMail(User user, Checkout checkout) {
		try {
			mailSender.sendMail(user, checkout, null, 3, null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendRegistrationConfirmationMail(User user, String applicationUrl) {
		try {
			mailSender.sendMail(user, null, applicationUrl, 0, null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendRegistrationCompleteMail(User user, String applicationUrl) {
		try {
			mailSender.sendMail(user, null, applicationUrl, 1, null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendDueDateAlertMail(Checkout checkout, Integer dueWithin) {
		try {
//			mailSender.sendMail(checkout.getUser(),  null, "URK", 1, null);
			mailSender.sendMail(checkout.getUser(), checkout, null, 4, dueWithin);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendBookAvailableMail(Checkout checkout, Integer dueWithin) {
		try {
			mailSender.sendMail(checkout.getUser(), checkout, null, 5, dueWithin);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
