package edu.sjsu.cmpe275.project.util;

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
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 *
 */
@Component
public class CustomMailSender {

	@Autowired
	private UserService service;

	@Autowired
	private MessageSource messages;

	@Autowired
	private Environment env;

	@Autowired
	private MailSender javaMailSender;

	@Async
	public Future<Void> sendMail(User user, Checkout checkout, String appUrl, int choice, Integer dueWithin)
			throws InterruptedException {

		final String token = UUID.randomUUID().toString();
		service.createVerificationTokenForUser(user, token);

		switch (choice) {
		case 0:
			final SimpleMailMessage email = constructEmailMessageVerify(user, token, appUrl);
			javaMailSender.send(email);
			break;

		case 1:
			final SimpleMailMessage cEmail = constructEmailMessageComplete(user, appUrl);
			javaMailSender.send(cEmail);
			break;

		case 2:
			final SimpleMailMessage checkoutEmail = constructEmailMessageCheckout(user, checkout);
			javaMailSender.send(checkoutEmail);
			break;

		case 3:
			final SimpleMailMessage returnEmail = constructEmailMessageBookReturn(user, checkout);
			javaMailSender.send(returnEmail);
			break;

		case 4:
			final SimpleMailMessage dueDateAlertEmail = constructEmailMessageAlert(dueWithin, checkout);
			javaMailSender.send(dueDateAlertEmail);
			break;
		
		case 5:
			final SimpleMailMessage bookAvailableAlertEmail = constructBookAvailableAlert(dueWithin, checkout);
			javaMailSender.send(bookAvailableAlertEmail);
			break;

		default:
			break;
		}
		System.out.println("Execute method asynchronously. " + Thread.currentThread().getName());
		return new AsyncResult<Void>(null);
	}

	/**
	 * @param dueWithin
	 * @param checkout
	 * @return
	 */
	private SimpleMailMessage constructBookAvailableAlert(Integer dueWithin, Checkout checkout) {

		final String recipientAddress = checkout.getUser().getEmail();
		final String subject = "Book Available Notification";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);

		email.setText("Book with the following details is available for checkout now. Please checkout this book before " + dueWithin + " days:\r\n "
				 + "\r\nTitle : " + checkout.getBook().getTitle() + "\r\nAuthor : " + checkout.getBook().getAuthor() + "\r\nPublisher : "
				+ checkout.getBook().getPublisher() + "\r\nPublication Year : "
				+ checkout.getBook().getPublicationYear());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	/**
	 * @param dueWithin
	 * @param checkout
	 * @return
	 */
	private SimpleMailMessage constructEmailMessageAlert(int dueWithin, Checkout checkout) {

		final String recipientAddress = checkout.getUser().getEmail();
		final String subject = "Due Date Alert";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);

		Date dueDate = DateUtils.addMonths(checkout.getCheckoutDate(), 1);
		email.setText("Book with the following details is due within " + dueWithin + " days:\r\n "
				+ checkout.getBook().getTitle() + "\r\nAuthor : " + checkout.getBook().getAuthor() + "\r\nPublisher : "
				+ checkout.getBook().getPublisher() + "\r\nPublication Year : "
				+ checkout.getBook().getPublicationYear() + "\r\nCheckout Date : " + checkout.getCheckoutDate()
				+ "\r\nDue Date : " + dueDate);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	/**
	 * Constructs the email message containing the book return details for the
	 * user
	 * 
	 * @param user
	 *            User that wants to return the book
	 * @param checkout
	 *            Checkout entity containing details about the book checked out
	 * @return SimpleMailMessage with all the fields set(subject, message, to,
	 *         from)
	 */
	private final SimpleMailMessage constructEmailMessageBookReturn(final User user, final Checkout checkout) {
		final String recipientAddress = user.getEmail();
		final String subject = "Return Confirmation";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(
				"Book returned successfully:\r\n " + "\r\nTitle : " + checkout.getBook().getTitle() + "\r\nAuthor : "
						+ checkout.getBook().getAuthor() + "\r\nPublisher : " + checkout.getBook().getPublisher()
						+ "\r\nPublication Year : " + checkout.getBook().getPublicationYear());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	/**
	 * Constructs the email message containing the details about the book
	 * checked out for a given user
	 * 
	 * @param user
	 *            User that wants to checkout the book
	 * @param checkout
	 *            Checkout entity containing details about the book checked out
	 * @return SimpleMailMessage with all the fields set(subject, message, to,
	 *         from)
	 */
	private final SimpleMailMessage constructEmailMessageCheckout(final User user, final Checkout checkout) {
		final String recipientAddress = user.getEmail();
		final String subject = "Checkout Confirmation";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);

		Date dueDate = DateUtils.addMonths(checkout.getCheckoutDate(), 1);
		email.setText(
				"Checked out successfully: \r\n" + "\r\nTitle : " + checkout.getBook().getTitle() + "\r\nAuthor : "
						+ checkout.getBook().getAuthor() + "\r\nPublisher : " + checkout.getBook().getPublisher()
						+ "\r\nPublication Year : " + checkout.getBook().getPublicationYear() + "\r\nCheckout Date : "
						+ checkout.getCheckoutDate() + "\r\nDue Date : " + dueDate);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	/**
	 * Constructs the email for the registration complete
	 * 
	 * @param user
	 *            Registered user
	 * @param appUrl
	 *            Application url to login into
	 * @return SimpleMailMessage with all the fields set(subject, message, to,
	 *         from)
	 */
	private SimpleMailMessage constructEmailMessageComplete(User user, String appUrl) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Complete";
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		String ms = "You can now login to your account by visiting \r\n " + appUrl + "/login";
		email.setSubject(subject);
		email.setText(ms);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}

	/**
	 * Constructs the email for the registration confirmation
	 * 
	 * @param user
	 *            User that wants to register
	 * @param token
	 *            The verification token
	 * @param appUrl
	 *            Application url with the verification token to verify the
	 *            account
	 * @return SimpleMailMessage with all the fields set(subject, message, to,
	 *         from)
	 */
	private final SimpleMailMessage constructEmailMessageVerify(final User user, final String token,
			final String appUrl) {
		final String recipientAddress = user.getEmail();
		final String subject = "Registration Confirmation";
		final String confirmationUrl = appUrl + "/registrationConfirm.html?token=" + token;
		final String message = messages.getMessage("message.regSucc", null, Locale.getDefault());
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setText(message + " \r\n" + confirmationUrl);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
}
