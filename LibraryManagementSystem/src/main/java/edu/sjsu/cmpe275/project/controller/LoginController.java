package edu.sjsu.cmpe275.project.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import ch.qos.logback.core.net.SyslogOutputStream;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.UserProfile;
import edu.sjsu.cmpe275.project.model.VerificationToken;
import edu.sjsu.cmpe275.project.notification.BookNotification;
import edu.sjsu.cmpe275.project.notification.CustomMailSender;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.CheckoutService;
import edu.sjsu.cmpe275.project.service.NotificationService;
import edu.sjsu.cmpe275.project.service.UserProfileService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.validation.UserValidator;
import edu.sjsu.cmpe275.project.dao.BookCopyDao;
import edu.sjsu.cmpe275.project.dao.CheckoutDao;
import edu.sjsu.cmpe275.project.dao.MyCalendarDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.MyCalendar;

/**
 * @author Onkar Ganjewar
 * @author Shreya Prabhu
 *
 */
@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class LoginController {

	@Autowired
	BookNotification bookNotification;

	@Autowired
	UserService userService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	BookService bookService;

	@Autowired
	CheckoutDao checkoutDao;

	@Autowired
	MyCalendarDao myCalendarDao;

	@Autowired
	CheckoutService checkoutService;

	HttpSession session;

	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;

	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	@Autowired
	private UserValidator customValidator;

	@Autowired
	private BookCopyDao bookCopyDao;
//
//	@RequestMapping(value = "/admin", method = RequestMethod.GET)
//	public String adminPage(ModelMap model) {
//		List<Book> books = bookService.findAllBooks();
//		model.addAttribute("books", books);
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		model.addAttribute("user", currentuser.getFirstName());
//
//		return "admin";
//	}
//
//	@RequestMapping(value = "/user/confirm-checkout-book-{id}", method = RequestMethod.GET)
//	public @ResponseBody String checkoutTest(@PathVariable("id") String id, @RequestParam("name") String userid,
//			ModelMap model) {
//		int userId = Integer.parseInt(userid);
//		int bookId = Integer.parseInt(id);
//
//		List<BookCopy> checkoutBookCopiesList = new ArrayList<BookCopy>();
//
//		// Get the book from the book id
//		Book book = bookService.findById(Integer.toString(bookId));
//
//		// Get the list of all the copies for the given book
//		List<BookCopy> allBookCopies = bookCopyDao.findByBook(book);
//
//		// Get the list of checked out copies for the given book
//		List<Checkout> checkoutBooksList = checkoutService.findByBookId(bookId);
//
//		// Check whether any copy of the given book is available for rent
//		if (checkoutBooksList != null && !checkoutBooksList.isEmpty()) {
//			for (Checkout checkout : checkoutBooksList) {
//				Date cdate = checkout.getCheckoutDate();
//				System.out.println(cdate);
//			}
//			if (allBookCopies.size() <= checkoutBooksList.size()) {
//				book.setAvailability("Not Available");
//				// available = false;
//				bookService.updateBook(book);
//				return "Failure";
//			}
//			// Get the list of book copies for the given checked-out book
//			for (Checkout checkout2 : checkoutBooksList) {
//				checkoutBookCopiesList.add(checkout2.getCopy());
//			}
//		}
//
//		// Compare all the book copies
//		// and checked-out book copies for the given book
//		// and remove the checked-out copies from the allCopies of the given
//		// book
//		allBookCopies.removeAll(checkoutBookCopiesList);
//		List<BookCopy> diffCopies = allBookCopies;
//
//		System.out.println(diffCopies);
//
//		// Loan the first different book copy to the given user
//		User user = userService.findById(userId);
//		List<Date> dates = new ArrayList<Date>();
//
//		// Check whether the user holds the different copy of the given book
//		List<Checkout> checkoutUsersList = checkoutService.findByUserId(userId);
//		if (checkoutUsersList != null && !checkoutUsersList.isEmpty()) {
//			if (checkoutUsersList.size() > 10)
//				return "TotalCheckoutLimit";
//			for (Checkout checkout : checkoutUsersList) {
//				Date userCheckOutsDate = checkout.getCheckoutDate();
//				dates.add(userCheckOutsDate);
//			}
//		}
//		// Sort the checkout dates for current user
//		Collections.sort(checkoutUsersList, new Comparator<Checkout>() {
//			public int compare(Checkout m1, Checkout m2) {
//				return m1.getCheckoutDate().compareTo(m2.getCheckoutDate());
//			}
//		});
//
//		// First remove all the records of the calendar entity
//		myCalendarDao.removeAll();
//
//		// Get the checkout dates from the list of checked out
//		// books for the given user
//		for (Checkout checkout : checkoutUsersList) {
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(checkout.getCheckoutDate());
//			int day = cal.get(Calendar.DAY_OF_MONTH);
//			int month = cal.get(Calendar.MONTH);
//			int year = cal.get(Calendar.YEAR);
//
//			// Save all the checkout dates for the given user
//			MyCalendar myCal = new MyCalendar();
//			myCal.setDay(day);
//			myCal.setMonth(month);
//			myCal.setYear(year);
//			myCalendarDao.insert(myCal);
//		}
//
//		// Get the current date
//		Date currentDate = new Date();
//		Calendar currentCal = Calendar.getInstance();
//		currentCal.setTime(currentDate);
//		int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
//		int currentMonth = currentCal.get(Calendar.MONTH);
//		int currentYear = currentCal.get(Calendar.YEAR);
//		MyCalendar myCurrentCal = new MyCalendar();
//		myCurrentCal.setDay(currentDay);
//		myCurrentCal.setMonth(currentMonth);
//		myCurrentCal.setYear(currentYear);
//
//		// Find the list of records from the MyCalendar entity
//		// matching the current date
//		List<MyCalendar> returnCals = myCalendarDao.findByCurrentTime(myCurrentCal);
//
//		// If the user has checked out 5 books for the given day
//		// then throw error
//		if (returnCals.size() >= 5)
//			return "DayCheckoutLimit";
//
//		// Insert the new checkout record in the database
//		Checkout checkout = new Checkout();
//		checkout.setBook(book);
//		checkout.setCopy(diffCopies.get(0));
//		checkout.setCheckoutDate(currentDate);
//		checkout.setUser(user);
//		checkout.setBookId(bookId);
//		checkout.setUserId(userId);
//		List<Checkout> checkoutCopies = new ArrayList<Checkout>();
//		checkoutCopies.add(checkout);
//		book.setCheckoutCopies(checkoutCopies);
//		user.setCheckoutCopies(checkoutCopies);
//		try {
//			checkoutDao.insert(checkout);
//		} catch (Exception e) {
//			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
//				return "Duplicate";
//			}
//			return "Failure";
//		}
//
//		try {
//			bookNotification.sendMail(checkout.getUser(), checkout, 0);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// Update the respective book and user entities to
//		// avoid making them transient
//		bookService.updateBook(book);
//		userService.updateUser(user);
//
//		return "Success";
//	}
//
//	@RequestMapping(value = "/home", method = RequestMethod.GET)
//	public String usersPage(ModelMap model) {
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		model.addAttribute("user", currentuser.getFirstName());
//		model.addAttribute("useremail", getPrincipal());
//		return "users";
//	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String demoPage() {
		// model.addAttribute("user", getPrincipal());
		System.out.println("ASDSDA");
		return "login";
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public String signUp(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "signup";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public String signUp_POST(@Valid User user, BindingResult result, ModelMap model,
			final HttpServletRequest request) {

		Set<UserProfile> profileSet = new HashSet<UserProfile>();

		customValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}

		if (user.getUserProfiles().isEmpty()) {
			// /** If the user owns an sjsu email id then he/she is
			// automatically
			// gets a librarian account. */
			if (user.getEmail().contains("@sjsu.edu")) {
				UserProfile profile = new UserProfile();
				profile = userProfileService.findByType("ADMIN");
				profileSet.add(profile);
				user.setUserProfiles(profileSet);
			} else {
				UserProfile profile = new UserProfile();
				profile = userProfileService.findByType("USER");
				profileSet.add(profile);
				user.setUserProfiles(profileSet);
			}
		}

		userService.saveUser(user);

		long startTime = System.currentTimeMillis();
		System.out.println("Before Execute method asynchronously. " + Thread.currentThread().getName());
		notificationService.sendRegistrationConfirmationMail(user,getAppUrl(request));

//		Collection<Future<Void>> futures = new ArrayList<Future<Void>>();
//		try {
//			futures.add(notificationService.sendMail(user, getAppUrl(request), 0));
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");

		model.addAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", user.getFirstName());
		return "signupsuccess";
	}

	@RequestMapping(value = "/registrationConfirm.html", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token,
			final HttpServletRequest req) {
		Locale locale = request.getLocale();

		VerificationToken verificationToken = userService.getVerificationToken(token);
		if (verificationToken == null) {
			String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
			model.addAttribute("message", message);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
			// return "badToken";
		}

		User user = verificationToken.getUser();
		Calendar cal = Calendar.getInstance();
		if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			String messageValue = messageSource.getMessage("auth.message.expired", null, locale);
			model.addAttribute("message", messageValue);
			return "redirect:/badUser.html?lang=" + locale.getLanguage();
		}

		user.setEnabled(true);
		userService.updateUser(user);
//
//		Collection<Future<Void>> futures = new ArrayList<Future<Void>>();
		notificationService.sendRegistrationConfirmationMail(user, getAppUrl(req));

//		try {
//			futures.add(notificationService.sendRegistrationConfirmationMail(user, getAppUrl(req)));
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return "activateRegistration";
	}

	private String getAppUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}

	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests. If users is already logged-in and
	 * tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(SecurityContextHolderAwareRequestWrapper request) {

		boolean adminExists = false;
		boolean userExists = false;
		List<UserProfile> allProfiles = new ArrayList<UserProfile>();

		// Populate user profiles if not already present
		allProfiles = userProfileService.findAll();
		for (UserProfile u : allProfiles) {
			if (u.getType().equals("ADMIN")) {
				adminExists = true;
				continue;
			} else if (u.getType().equals("USER")) {
				userExists = true;
				continue;
			}
		}

		UserProfile up = new UserProfile();
		up.setType("ADMIN");
		if (!adminExists)
			userProfileService.saveProfile(up);

		UserProfile usp = new UserProfile();
		usp.setType("USER");
		if (!userExists)
			userProfileService.saveProfile(usp);

		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		} else if (request.isUserInRole("ROLE_ADMIN")) {
			return "admin";
		} else if (request.isUserInRole("ROLE_USER")) {
			return "users";
		} else {
			return "login";
		}
	}

	protected boolean hasRole(String role) {
		// get security context from thread local
		SecurityContext context = SecurityContextHolder.getContext();
		if (context == null)
			return false;

		Authentication authentication = context.getAuthentication();
		if (authentication == null)
			return false;

		for (GrantedAuthority auth : authentication.getAuthorities()) {
			if (role.equals(auth.getAuthority()))
				return true;
		}

		return false;
	}

	/**
	 * This method handles logout requests. Toggle the handlers if you are
	 * RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			// new SecurityContextLogoutHandler().logout(request, response,
			// auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails) principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
//
//	@RequestMapping(value = "/newBook", method = RequestMethod.GET)
//	public String addNewBookPage(ModelMap model) {
//		Book books = new Book();
//		model.addAttribute("book", books);
//		return "newBook";
//	}
//
//	@RequestMapping(value = { "/newBook" }, method = RequestMethod.POST)
//	public String newBook_POST(Book book, BindingResult result, ModelMap model, final HttpServletRequest request,
//			@ModelAttribute("copies") String copy) {
//
//		boolean NaN = false;
//		int copies = 0;
//		// Check the input no of copies
//		try {
//			copies = Integer.parseInt(copy);
//		} catch (NumberFormatException e) {
//			// If not a valid number or null string, then create only one copy
//			// by default
//			NaN = true;
//		}
//		Integer returnBookId = bookService.saveBook(book);
//		Book returnBook = bookService.findById(Integer.toString(returnBookId));
//
//		if (returnBookId < 0) {
//			return "Error";
//		}
//		if (!NaN) {
//			// If a valid number then create the specified number of copies
//			for (int i = 0; i < copies; i++) {
//				BookCopy bookCopy = new BookCopy();
//				bookCopy.setBooks(returnBook);
//				bookCopyDao.save(bookCopy);
//			}
//		} else {
//			BookCopy bookCopy = new BookCopy();
//			bookCopy.setBooks(returnBook);
//			bookCopyDao.save(bookCopy);
//		}
//		return "redirect:/admin";
//	}
//
//	/**
//	 * This method will provide the medium to update an existing user.
//	 */
//	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.GET)
//	public String editBook(@PathVariable String id, ModelMap model) {
//		Book book = bookService.findById(id);
//		int copies = 1;
//		List<BookCopy> returnCopies = bookCopyDao.findByBook(book);
//		copies = returnCopies.size();
//		model.addAttribute("book", book);
//		model.addAttribute("copies", copies);
//		model.addAttribute("edit", true);
//		return "editBook";
//	}
//
//	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.POST)
//	public String updateBook(Book book, BindingResult result, ModelMap model, @PathVariable String id,
//			@RequestParam(value = "cn", required = false) String cn) {
//		boolean NaN = false;
//		int copies = 0;
//		// Check the input no of copies
//		try {
//			copies = Integer.parseInt(cn);
//		} catch (NumberFormatException e) {
//			// If not a valid number or null string, then DO NOT update copies
//			NaN = true;
//		}
//		bookService.updateBook(book);
//		Book returnBook = bookService.findById(id);
//
//		if (!NaN) {
//			// If a valid number then create the specified number of copies
//			// get the number of copies existing for that book
//			List<BookCopy> copiesList = bookCopyDao.findByBook(book);
//			for (BookCopy bookCopy : copiesList) {
//				bookCopyDao.deleteBookCopy(bookCopy);
//			}
//			for (int i = 0; i < copies; i++) {
//				BookCopy bookCopy = new BookCopy();
//				bookCopy.setBooks(returnBook);
//				bookCopyDao.save(bookCopy);
//			}
//		} else {
//			BookCopy bookCopy = new BookCopy();
//			bookCopy.setBooks(returnBook);
//			bookCopyDao.save(bookCopy);
//		}
//
//		return "redirect:/admin";
//	}
//
//	@RequestMapping(value = { "/delete-book-{id}" }, method = RequestMethod.GET)
//	public String deleteBook(@PathVariable String id, ModelMap mo) {
//
//		boolean exceptionOccured = false;
//		try {
//			bookService.deleteBook(Integer.parseInt(id));
//		} catch (Exception e) {
//			exceptionOccured = true;
//			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
//				mo.addAttribute("val1", "failure");
//			} else {
//				mo.addAttribute("val1", "exception");
//			}
//		}
//
//		if (!exceptionOccured)
//			mo.addAttribute("val1", "Success");
//		List<Book> books = bookService.findAllBooks();
//		mo.addAttribute("books", books);
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		mo.addAttribute("user", currentuser.getFirstName());
//		return "admin";
//	}

	@RequestMapping(value = { "/delete-book-search-{id}" }, method = RequestMethod.GET)
	public String deleteBookFromSearch(@PathVariable String id, @RequestParam("name") String bookTitle, ModelMap mo) {

		boolean exceptionOccured = false;
		try {
			bookService.deleteBook(Integer.parseInt(id));
		} catch (Exception e) {
			exceptionOccured = true;
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				mo.addAttribute("val1", "failure");
			} else {
				mo.addAttribute("val1", "exception");
			}
		}

		if (!exceptionOccured)
			mo.addAttribute("val1", "Success");
		List<Book> books = (List<Book>) bookService.findByTitle(bookTitle);
		mo.addAttribute("books", books);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		mo.addAttribute("user", currentuser.getFirstName());
		return "searchResults";
	}

	/**
	 * This method returns true if users is already authenticated [logged-in],
	 * else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}

	@RequestMapping(value = "/bookInfo-{isbn}", method = RequestMethod.GET)
	public String getBookInfo(@PathVariable String isbn, ModelMap model) throws Exception {
		// String isbn = "0201633612";
		// @RequestParam("isbn") String isbn
		URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);

		// read from the URL
		Scanner scan = new Scanner(url.openStream());
		String str = new String();
		while (scan.hasNext())
			str += scan.nextLine();
		scan.close();

		// build a JSON object
		JSONObject obj = new JSONObject(str);

		Object title = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").get("title");
		System.out.println("Title = " + title);
		String titleString = title.toString();

		Object publisher = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").get("publisher");
		System.out.println("Publisher = " + publisher);
		String publisherString = publisher.toString();

		Object publishDate = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo")
				.get("publishedDate");
		System.out.println("Date published = " + publishDate);
		String publishedString = publishDate.toString();

		JSONArray arr = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors");
		int limit = arr.length();
		List<String> authorsList = new ArrayList<String>();

		for (int i = 0; i < limit; i++) {
			Object val = arr.get(i);
			// System.out.println(val);
			authorsList.add(val.toString());
		}
		String authorString = "";

		for (String string : authorsList) {
			System.out.println("Authors name = " + string);
			authorString += string + ", ";
		}

		System.out.println(authorString);

		Book book = new Book();
		book.setPublisher(publisherString);
		book.setPublicationYear(publishedString);
		book.setTitle(titleString);
		book.setAuthor(authorString);
		model.addAttribute("book", book);
		return "newBook";
	}

	@RequestMapping(value = { "/bookInfo-{isbn}" }, method = RequestMethod.POST)
	public String newBookByISBN_POST(Book book, BindingResult result, ModelMap model, final HttpServletRequest request,
			@ModelAttribute("copies") String copy) {

		boolean NaN = false;
		int copies = 0;
		// Check the input no of copies
		try {
			copies = Integer.parseInt(copy);
		} catch (NumberFormatException e) {
			// If not a valid number or null string, then create only one copy
			// by default
			NaN = true;
		}
		Integer returnBookId = bookService.saveBook(book);
		Book returnBook = bookService.findById(Integer.toString(returnBookId));

		if (returnBookId < 0) {
			return "Error";
		}
		if (!NaN) {
			// If a valid number then create the specified number of copies
			for (int i = 0; i < copies; i++) {
				BookCopy bookCopy = new BookCopy();
				bookCopy.setBooks(returnBook);
				bookCopyDao.save(bookCopy);
			}
		} else {
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(returnBook);
			bookCopyDao.save(bookCopy);
		}
		return "redirect:/admin";
	}

//	@RequestMapping(value = "/search-book-{txtSearch:.+}", method = RequestMethod.GET)
//	public String searchBook(@PathVariable String txtSearch, ModelMap model) {
//		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);
//
//		model.addAttribute("books", books);
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		model.addAttribute("user", currentuser.getFirstName());
//		return "searchResults";
//	}

//	@RequestMapping(value = "/user/search-book-{txtSearch}", method = RequestMethod.GET)
//	public String searchBookForUser(@PathVariable String txtSearch, ModelMap model) {
//		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		model.addAttribute("user", currentuser.getFirstName());
//		model.addAttribute("books", books);
//		model.addAttribute("useremail", getPrincipal());
//		return "users";
//	}
//
//	@RequestMapping(value = "/user/checkout-book-{id}", method = RequestMethod.GET)
//	public String checkoutBookForUser(@PathVariable("id") String id, @RequestParam("name") String username,
//			ModelMap model) {
//		Book book = new Book();
//		book = (Book) bookService.findById(id);
//
//		Date dueDate = DateUtils.addMonths(new Date(), 1);
//		User user = userService.findByEmail(username);
//
//		model.addAttribute("userid", user.getId());
//		model.addAttribute("user", user.getFirstName());
//		model.addAttribute("due", dueDate.toString());
//		model.addAttribute("book", book);
//		return "bookCheckoutWindow";
//	}
//
//	@RequestMapping(value = "/user/viewCheckedOutBooks", method = RequestMethod.GET)
//	public String userCheckedOutBooks(@RequestParam("name") String user, ModelMap model) {
//
//		User current_user = userService.findByEmail(user);
//		List<Checkout> checkedOutBooksList = checkoutService.findByUserId(current_user.getId());
//		List<Book> books = new ArrayList<Book>();
//		for (Checkout checkout : checkedOutBooksList) {
//			books.add(checkout.getBook());
//		}
//		model.addAttribute("books", books);
//		String email_user = getPrincipal();
//		User currentuser = userService.findByEmail(email_user);
//		model.addAttribute("user", currentuser.getFirstName());
//		model.addAttribute("useremail", getPrincipal());
//		return "checkedOutBooks";
//	}
//
//	@RequestMapping(value = "/return-book-{id}", method = RequestMethod.GET)
//	public String returnBook(@PathVariable("id") String id, @RequestParam("name") String username, ModelMap model) {
//		Book book = new Book();
//		book = (Book) bookService.findById(id);
//		User user = userService.findByEmail(username);
//		Checkout returnCopy = new Checkout();
//		List<Checkout> chCopies = checkoutService.findByUserId(user.getId());
//		for (Checkout checkout : chCopies) {
//			if (checkout.getUserId() == user.getId() && checkout.getBookId() == book.getId()) {
//				returnCopy = checkout;
//				break;
//			}
//		}
//		System.out.println(returnCopy);
//		checkoutService.removeCheckout(returnCopy);
//		try {
//			bookNotification.sendMail(returnCopy.getUser(), returnCopy, 1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		model.addAttribute("val1", "success");
//		model.addAttribute("userid", user.getId());
//		String email_user = getPrincipal();
//		model.addAttribute("user", user.getFirstName());
//		List<Checkout> checkedOutBooks = checkoutService.findByUserId(user.getId());
//		List<Book> books = new ArrayList<Book>();
//		for (Checkout checkout : checkedOutBooks) {
//			books.add(checkout.getBook());
//		}
//		model.addAttribute("books", books);
//		return "checkedOutBooks";
//	}

//	@RequestMapping(value = "/confirmedCheckout", method = RequestMethod.GET)
//	public String confirmedcheckout(@RequestParam("bookId") String id, @RequestParam("userId") String userid,
//			ModelMap model) {
//		System.out.println("BOOK" + id + "USER" + userid);
//		Book book = new Book();
//		book = (Book) bookService.findById(id);
//
//		Date dueDate = DateUtils.addMonths(new Date(), 1);
//		User user = userService.findById(Integer.parseInt(userid));
//
//		model.addAttribute("useremail", getPrincipal());
//		model.addAttribute("userid", user.getId());
//		model.addAttribute("user", user.getFirstName());
//		model.addAttribute("due", dueDate.toString());
//		model.addAttribute("book", book);
//		return "confirmedCheckoutScreen";
//	}
}