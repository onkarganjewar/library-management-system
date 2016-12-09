package edu.sjsu.cmpe275.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.notification.CustomMailSender;

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
			mailSender.sendMail(user, checkout,null, 2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendReturnMail(User user, Checkout checkout) {
		try {
			mailSender.sendMail(user, checkout, null, 3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void sendRegistrationConfirmationMail(User user, String applicationUrl) {
		try {
			mailSender.sendMail(user,null, applicationUrl, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendRegistrationCompleteMail(User user, String applicationUrl) {
		try {
			mailSender.sendMail(user, null, applicationUrl, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
