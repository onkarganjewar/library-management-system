package edu.sjsu.cmpe275.project.controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.UserProfile;
import edu.sjsu.cmpe275.project.model.VerificationToken;
import edu.sjsu.cmpe275.project.notification.CustomMailSender;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.CheckoutService;
import edu.sjsu.cmpe275.project.service.UserProfileService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.validation.UserValidator;
import edu.sjsu.cmpe275.project.dao.BookCopyDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.dao.CheckoutDao;
import edu.sjsu.cmpe275.project.dao.UserDao;

/**
 * @author Onkar Ganjewar
 * @author Shreya Prabhu
 *
 */
@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	UserService userService;

	@Autowired
	CustomMailSender mailSender;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	BookService bookService;

	@Autowired
	CheckoutDao checkoutDao;

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

	@Autowired
	private UserDao userDao;

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		List<Book> books = bookService.findAllBooks();
		model.addAttribute("books", books);
		return "admin";
	}

	@RequestMapping(value = "/demo/ch", method = RequestMethod.GET)
	public String checkoutTest() {
		int userId = 2;
		int bookId = 13;

		List<BookCopy> checkoutBookCopiesList = new ArrayList<BookCopy>();
		
		// Get the book from the book id
		Book book = bookService.findById(Integer.toString(bookId));

		// Get the list of all the copies for the given book
		List<BookCopy> allBookCopies = bookCopyDao.findByBook(book);

		// Get the list of checked out copies for the given book
		List<Checkout> checkoutBooksList = checkoutService.findByBookId(bookId);

		// Check whether any copy of the given book is available for rent
		if (checkoutBooksList!=null && !checkoutBooksList.isEmpty()) {
			if (allBookCopies.size() <= checkoutBooksList.size()) {
				book.setAvailability("Not Available");
//				available = false;
				bookService.updateBook(book);
				return "Book-Copy-Not-Available";
			}
			// Get the list of book copies for the given checked-out book
			for (Checkout checkout2 : checkoutBooksList) {
				checkoutBookCopiesList.add(checkout2.getCopy());
			}
		}
	
		// Compare all the book copies and checked-out book copies for the given book
		// and remove the checked-out copies from the allCopies of the given book
		allBookCopies.removeAll(checkoutBookCopiesList);
		List<BookCopy> diffCopies = allBookCopies;
		
		System.out.println(diffCopies);
		
		// Loan the first different book copy to the given user
		User user = userService.findById(userId);
		
		// Check whether the user holds the different copy of the given book
//		List<Checkout> checkoutUsersList = checkoutService.findByUserId(userId);
//		if (checkoutUsersList!=null && !checkoutUsersList.isEmpty()) {
//			for (Checkout checkout : checkoutUsersList) {
//				long checkoutUserId = checkout.getUserId();
//				if (checkoutUserId == userId) {
//					userAlreadyOwnsTheBook = true;
//					break;
//				}
//			}
//		}
		
		
		Checkout checkout = new Checkout();
		checkout.setBook(book);
		checkout.setCopy(diffCopies.get(0));
		checkout.setUser(user);
		checkout.setBookId(bookId);
		checkout.setUserId(userId);
		List<Checkout> checkoutCopies = new ArrayList<Checkout>();
		checkoutCopies.add(checkout);
		book.setCheckoutCopies(checkoutCopies);
		user.setCheckoutCopies(checkoutCopies);
		try {
			checkoutDao.insert(checkout);
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				return "{\"Status\":\"Duplicate\"}";
			}
			return "{\"Status\":\"Failure\"}";
		}

		bookService.updateBook(book);
		userService.updateUser(user);

		Book returnBook = bookService.findById(Integer.toString(bookId));
		return "admin";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String usersPage(ModelMap model) {
		model.addAttribute("user", getPrincipal());
		return "users";
	}

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
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

		customValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}

		userService.saveUser(user);

		long startTime = System.currentTimeMillis();
		System.out.println("Before Execute method asynchronously. " + Thread.currentThread().getName());

		Collection<Future<Void>> futures = new ArrayList<Future<Void>>();

		try {
			futures.add(mailSender.sendMail(user, getAppUrl(request)));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,
		// request.getLocale(), getAppUrl(request)));

		long endTime = System.currentTimeMillis();

		System.out.println("That took " + (endTime - startTime) + " milliseconds");

		model.addAttribute("success",
				"User " + user.getFirstName() + " " + user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", user.getFirstName());
		return "signupsuccess";
	}

	@RequestMapping(value = "/registrationConfirm.html", method = RequestMethod.GET)
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token) {
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
		return "login";
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
	public String loginPage() {

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
		} else {
			return "redirect:/list";
		}
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

	@RequestMapping(value = "/newBook", method = RequestMethod.GET)
	public String addNewBookPage(ModelMap model) {
		Book books = new Book();
		model.addAttribute("book", books);
		return "newBook";
	}

	@RequestMapping(value = { "/newBook" }, method = RequestMethod.POST)
	public String newBook_POST(Book book, BindingResult result, ModelMap model, final HttpServletRequest request,
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
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(book);
			bookCopyDao.save(bookCopy);
		}
		bookService.saveBook(book);

		if (!NaN) {
			// If a valid number then create the specified number of copies
			for (int i = 0; i < copies; i++) {
				BookCopy bookCopy = new BookCopy();
				bookCopy.setBooks(book);
				bookCopyDao.save(bookCopy);
			}
		}
		return "redirect:/admin";
	}

	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.GET)
	public String editBook(@PathVariable String id, ModelMap model) {
		Book book = bookService.findById(id);
		int copies = 1;
		List<BookCopy> returnCopies = bookCopyDao.findByBook(book);
		copies = returnCopies.size();
		model.addAttribute("book", book);
		model.addAttribute("copies", copies);
		model.addAttribute("edit", true);
		return "editBook";
	}

	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.POST)
	public String updateBook(Book book, BindingResult result, ModelMap model, @PathVariable String id,
			@RequestParam(value = "cn", required = false) String cn) {
		boolean NaN = false;
		int copies = 0;
		// Check the input no of copies
		try {
			copies = Integer.parseInt(cn);
		} catch (NumberFormatException e) {
			// If not a valid number or null string, then DO NOT update copies
			NaN = true;
		}
		bookService.updateBook(book);

		if (!NaN) {
			// If a valid number then create the specified number of copies
			// get the number of copies existing for that book
			List<BookCopy> copiesList = bookCopyDao.findByBook(book);
			for (BookCopy bookCopy : copiesList) {
				bookCopyDao.deleteBookCopy(bookCopy);
			}
			for (int i = 0; i < copies; i++) {
				BookCopy bookCopy = new BookCopy();
				bookCopy.setBooks(book);
				bookCopyDao.save(bookCopy);
			}
		}

		return "redirect:/admin";
	}

	@RequestMapping(value = { "/delete-book-{id}" }, method = RequestMethod.GET)
	public String deleteBook(@PathVariable String id) {
		bookService.deleteBook(Integer.parseInt(id));
		return "redirect:/admin";
	}

	/**
	 * This method returns true if users is already authenticated [logged-in],
	 * else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authenticationTrustResolver.isAnonymous(authentication);
	}

	@RequestMapping(value = "/bookInfo", method = RequestMethod.GET)
	public String getBookInfo(@RequestParam("isbn") String isbn) throws Exception {

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

		Object publisher = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").get("publisher");
		System.out.println("Publisher = " + publisher);

		Object publishDate = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo")
				.get("publishedDate");
		System.out.println("Date published = " + publishDate);

		JSONArray arr = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors");
		int limit = arr.length();
		List<String> authorsList = new ArrayList<String>();

		for (int i = 0; i < limit; i++) {
			Object val = arr.get(i);
			// System.out.println(val);
			authorsList.add(val.toString());
		}
		for (String string : authorsList) {
			System.out.println("Authors name = " + string);
		}
		return null;
	}

	@RequestMapping(value = "/search-book-{txtSearch:.+}", method = RequestMethod.GET)
	public String searchBook(@PathVariable String txtSearch, ModelMap model) {
		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);

		model.addAttribute("books", books);
		return "admin";
	}

	@RequestMapping(value = "/user/search-book-{txtSearch}", method = RequestMethod.GET)
	public String searchBookForUser(@PathVariable String txtSearch, ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(txtSearch);

		List<Book> books = new ArrayList<Book>();
		books.add(book);

		model.addAttribute("books", books);
		return "users";
	}
	
	@RequestMapping(value = "/user/checkout-book-{id}", method = RequestMethod.GET)
	public String checkoutBookForUser(@PathVariable String id, ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(id);
	
        Date dueDate = DateUtils.addMonths(new Date(), 1);
        //created = LocalDate.now();
        //System.out.println("Date : " +dueDate);
        
		model.addAttribute("due",dueDate.toString());
		model.addAttribute("book",book);
		//model.addAttribute("widget",created);
		return "bookCheckoutWindow";
	}
	
	@RequestMapping(value = "/user/viewCheckedOutBooks-{user}", method = RequestMethod.GET)
	public String userCheckedOutBooks(@PathVariable String user,ModelMap model) {
		
		User current_user=userDao.findByEmail(user);
		
		
        
		return "checkedOutBooks";
	}
	
	
	@RequestMapping(value = "/demo-checkout-{timeDate}", method = RequestMethod.GET)
	public String demoCheckout(@PathVariable String timeDate,ModelMap model) throws ParseException {
		
		Date setDate = new Date(timeDate);
		
		//DateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss Z", Locale.US);
		//Date setDate = format.parse(timeDate);
		
	   Date now=new Date();
	   int result=(int) ((setDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
	   int result1=(int) ((setDate.getTime() - now.getTime())/(1000 * 60 * 60));

	   
	   System.out.println("RESULT"+result1);
		return "checkedOutBooks";
	}
}