package edu.sjsu.cmpe275.project.notification;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.CheckoutService;
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 *
 */
@Component
public class BookNotification {

	@Autowired
	private Environment env;

	@Autowired
	private MailSender javaMailSender;

	@Async
	public Future<Void> sendMail(User user, Checkout checkout, int choice) throws InterruptedException {

		switch (choice) {
		case 0:
			final SimpleMailMessage checkoutEmail = constructEmailMessage(user, checkout);
			javaMailSender.send(checkoutEmail);
			break;

		case 1:
			final SimpleMailMessage returnEmail = constructEmailMessageBookReturn(user, checkout);
			javaMailSender.send(returnEmail);
			break;
		default:
			break;
		}
		System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
		return new AsyncResult<Void>(null);
	}

	private final SimpleMailMessage constructEmailMessage(final User user, final Checkout checkout) {
		final String recipientAddress = user.getEmail();
		final String subject = "Checkout Confirmation";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);

		Date dueDate = DateUtils.addMonths(checkout.getCheckoutDate(), 1);
		email.setText("Checked out successfully: \r\n" + "\r\nTitle : " + checkout.getBook().getTitle() + "\r\nAuthor : "
				+ checkout.getBook().getAuthor() + "\r\nPublisher : " + checkout.getBook().getPublisher()
				+ "\r\nPublication Year : " + checkout.getBook().getPublicationYear() + "\r\nCheckout Date : "
				+ checkout.getCheckoutDate() + "\r\nDue Date : " + dueDate);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	private final SimpleMailMessage constructEmailMessageBookReturn(final User user, final Checkout checkout) {
		final String recipientAddress = user.getEmail();
		final String subject = "Return Confirmation";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText("Book returned successfully:\r\n " + "\r\nTitle : " + checkout.getBook().getTitle() + "\r\nAuthor : "
				+ checkout.getBook().getAuthor() + "\r\nPublisher : " + checkout.getBook().getPublisher()
				+ "\r\nPublication Year : " + checkout.getBook().getPublicationYear());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
}
