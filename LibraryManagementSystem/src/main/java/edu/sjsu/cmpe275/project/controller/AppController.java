package edu.sjsu.cmpe275.project.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import edu.sjsu.cmpe275.project.service.UserProfileService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.validation.UserValidator;
import edu.sjsu.cmpe275.project.dao.BookCopyDao;
import edu.sjsu.cmpe275.project.dao.BookDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;


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

	// @Autowired
	// private ApplicationEventPublisher eventPublisher;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	BookDao bookDao;
	
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

	@RequestMapping(value = "/admin", method = RequestMethod.GET)
	public String adminPage(ModelMap model) {
		List<Book> books=bookDao.findAllBooks();
		model.addAttribute("books",books);
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
	
//	@ModelAttribute("copies")
//	public List<UserProfile> initializeProfiles() {
//		return bookCopyDao.findByBook(book);
//	}
	
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
		Book books=new Book();
		model.addAttribute("book",books);
		return "newBook";
	}
	
	@RequestMapping(value = { "/newBook" }, method = RequestMethod.POST)
	public String newBook_POST(Book book, BindingResult result, ModelMap model,
			final HttpServletRequest request,  @ModelAttribute("copies") String copy) {

		boolean NaN = false;
		int copies=0;
		// Check the input no of copies
		try {
			copies = Integer.parseInt(copy);	
		} catch (NumberFormatException e) {
			// If not a valid number or null string, then create only one copy by default
			NaN = true;
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(book);
			bookCopyDao.save(bookCopy);
		}
		bookDao.save(book);

		if(!NaN) {
			// If a valid number then create the specified number of copies
			for (int i=0 ; i<copies; i++) {
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
    public String editBook(@PathVariable String id , ModelMap model) {
    	Book book= bookDao.findbyId(id);
    	int copies = 10;
//    	BookCopy returnCopy = bookCopyDao.findByBook(book);
//    	returnCopy.getBooks();
        model.addAttribute("book", book);
        model.addAttribute("copies", copies);
        model.addAttribute("edit", true);
        return "newBook";
    }
    
    @RequestMapping(value = { "/edit-book-{id}"}, method = RequestMethod.POST)
    public String updateBook(Book book, @ModelAttribute("copies") String copy,BindingResult result,
            ModelMap model, @PathVariable String id) {
 
        if (result.hasErrors()) {
            return "redirect:/admin";
        }
 
        /*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
        if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
            FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
            result.addError(ssoError);
            return "registration";
        }*/

        //bookDao.update(book);
       
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
		System.out.println("Title = "+title);

		Object publisher = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").get("publisher");
		System.out.println("Publisher = "+publisher);

		Object publishDate = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo")
				.get("publishedDate");
		System.out.println("Date published = "+publishDate);

		JSONArray arr = obj.getJSONArray("items").getJSONObject(0).getJSONObject("volumeInfo").getJSONArray("authors");
		int limit = arr.length();
		List<String> authorsList = new ArrayList<String>();

		for (int i = 0; i < limit; i++) {
			Object val = arr.get(i);
//			System.out.println(val);
			authorsList.add(val.toString());
		}
		for (String string : authorsList) {
			System.out.println("Authors name = " + string);
		}
		return null;
	}
	
	@RequestMapping(value = "/search-book-{txtSearch}", method = RequestMethod.GET)
	public String searchBook(@PathVariable String txtSearch, ModelMap model) {
		Book book = new Book();
		book = (Book) bookDao.findbyId(txtSearch);
		
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		
		model.addAttribute("books",books);
		return "admin";
	}
	
	@RequestMapping(value = "/user/search-book-{txtSearch}", method = RequestMethod.GET)
	public String searchBookForUser(@PathVariable String txtSearch, ModelMap model) {
		Book book = new Book();
		book = (Book) bookDao.findbyId(txtSearch);
		
		List<Book> books = new ArrayList<Book>();
		books.add(book);
		
		model.addAttribute("books",books);
		return "users";
	}
}