package edu.sjsu.cmpe275.project.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.sjsu.cmpe275.project.dao.CheckoutDao;
import edu.sjsu.cmpe275.project.dao.MyCalendarDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.MyCalendar;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.BookCopyService;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.CheckoutService;
import edu.sjsu.cmpe275.project.service.NotificationService;
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 */

@Controller
@RequestMapping("/patron")
public class PatronController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	CheckoutDao checkoutDao;

	@Autowired
	MyCalendarDao myCalendarDao;

	@Autowired
	CheckoutService checkoutService;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookCopyService bookCopyService;

	@Autowired
	private UserService userService;

	/**
	 * Renders the home page for the patron.
	 * @param model ModelMap to hold the object data.
	 * @return users.jsp
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String renderIndex(ModelMap model) {
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("useremail", getPrincipal());
		return "users";
	}
	

	/**
	 * Renders the retrieved results for the searched book by the title
	 * @param txtSearch Title of the book to be searched
	 * @param model ModelMap to hold the book object data
	 * @return users.jsp
	 */
	@RequestMapping(value = "/search-book-{txtSearch}", method = RequestMethod.GET)
	public String renderSearchedBooks(@PathVariable String txtSearch, ModelMap model) {
		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("books", books);
		model.addAttribute("useremail", getPrincipal());
		return "users";
	}
	
	/**
	 * Renders the book checkout confirmation form
	 * @param id Id of the book to be checked out
	 * @param username Name/Email of the user 
	 * @param model ModelMap to hold the book/user object data
	 * @return bookCheckoutWindow.jsp
	 */
	@RequestMapping(value = "/checkout-book-{id}", method = RequestMethod.GET)
	public String renderCheckoutConfirmation(@PathVariable("id") String id, @RequestParam("name") String username,
			ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(id);

		Date dueDate = DateUtils.addMonths(new Date(), 1);
		User user = userService.findByEmail(username);

		model.addAttribute("userid", user.getId());
		model.addAttribute("user", user.getFirstName());
		model.addAttribute("due", dueDate.toString());
		model.addAttribute("book", book);
		return "bookCheckoutWindow";
	}


	/**
	 * Checks out the particular book for the given user.
	 * @param id Id of the book to be checked out
	 * @param userid Id of the user that wants the book
	 * @return Success, if the book is checked out successfully
	 * <br> Failure, if there's any error in issuing of the book </br>
	 * Duplicate, if the book is already checked out 
	 */
	@RequestMapping(value = "/confirm-checkout-book-{id}", method = RequestMethod.GET)
	public @ResponseBody String checkoutBook (@PathVariable("id") String id, @RequestParam("name") String userid) {
		int userId = Integer.parseInt(userid);
		int bookId = Integer.parseInt(id);

		List<BookCopy> checkoutBookCopiesList = new ArrayList<BookCopy>();

		// Get the book from the book id
		Book book = bookService.findById(Integer.toString(bookId));

		// Get the list of all the copies for the given book
		List<BookCopy> allBookCopies = bookCopyService.findAllByBook(book);

		// Get the list of checked out copies for the given book
		List<Checkout> checkoutBooksList = checkoutService.findByBookId(bookId);

		// Check whether any copy of the given book is available for rent
		if (checkoutBooksList != null && !checkoutBooksList.isEmpty()) {
			for (Checkout checkout : checkoutBooksList) {
				Date cdate = checkout.getCheckoutDate();
				System.out.println(cdate);
			}
			if (allBookCopies.size() <= checkoutBooksList.size()) {
				book.setAvailability("Not Available");
				// available = false;
				bookService.updateBook(book);
				return "Failure";
			}
			// Get the list of book copies for the given checked-out book
			for (Checkout checkout2 : checkoutBooksList) {
				checkoutBookCopiesList.add(checkout2.getCopy());
			}
		}

		// Compare all the book copies
		// and checked-out book copies for the given book
		// and remove the checked-out copies from the allCopies of the given
		// book
		allBookCopies.removeAll(checkoutBookCopiesList);
		List<BookCopy> diffCopies = allBookCopies;

		System.out.println(diffCopies);

		// Loan the first different book copy to the given user
		User user = userService.findById(userId);
		List<Date> dates = new ArrayList<Date>();

		// Check whether the user holds the different copy of the given book
		List<Checkout> checkoutUsersList = checkoutService.findByUserId(userId);
		if (checkoutUsersList != null && !checkoutUsersList.isEmpty()) {
			if (checkoutUsersList.size() > 10)
				return "TotalCheckoutLimit";
			for (Checkout checkout : checkoutUsersList) {
				Date userCheckOutsDate = checkout.getCheckoutDate();
				dates.add(userCheckOutsDate);
			}
		}
		// Sort the checkout dates for current user
		Collections.sort(checkoutUsersList, new Comparator<Checkout>() {
			public int compare(Checkout m1, Checkout m2) {
				return m1.getCheckoutDate().compareTo(m2.getCheckoutDate());
			}
		});

		// First remove all the records of the calendar entity
		myCalendarDao.removeAll();

		// Get the checkout dates from the list of checked out
		// books for the given user
		for (Checkout checkout : checkoutUsersList) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(checkout.getCheckoutDate());
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int month = cal.get(Calendar.MONTH);
			int year = cal.get(Calendar.YEAR);

			// Save all the checkout dates for the given user
			MyCalendar myCal = new MyCalendar();
			myCal.setDay(day);
			myCal.setMonth(month);
			myCal.setYear(year);
			myCalendarDao.insert(myCal);
		}

		// Get the current date
		Date currentDate = new Date();
		Calendar currentCal = Calendar.getInstance();
		currentCal.setTime(currentDate);
		int currentDay = currentCal.get(Calendar.DAY_OF_MONTH);
		int currentMonth = currentCal.get(Calendar.MONTH);
		int currentYear = currentCal.get(Calendar.YEAR);
		MyCalendar myCurrentCal = new MyCalendar();
		myCurrentCal.setDay(currentDay);
		myCurrentCal.setMonth(currentMonth);
		myCurrentCal.setYear(currentYear);

		// Find the list of records from the MyCalendar entity
		// matching the current date
		List<MyCalendar> returnCals = myCalendarDao.findByCurrentTime(myCurrentCal);

		// If the user has checked out 5 books for the given day
		// then throw error
		if (returnCals.size() >= 5)
			return "DayCheckoutLimit";

		// Insert the new checkout record in the database
		Checkout checkout = new Checkout();
		checkout.setBook(book);
		checkout.setCopy(diffCopies.get(0));
		checkout.setCheckoutDate(currentDate);
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
				return "Duplicate";
			}
			return "Failure";
		}

		notificationService.sendCheckoutMail(checkout.getUser(), checkout);
		// Update the respective book and user entities to
		// avoid making them transient
		bookService.updateBook(book);
		userService.updateUser(user);
		return "Success";
	}

	/**
	 * Get the UserName of the logged-in user.
	 * @return the UserName of the logged-in user
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
	
	/**
	 * Renders the checkout successful screen with the book details
	 * @param id Id of the book checked out
	 * @param userid Id of the user
	 * @param model ModelMap to hold the book/user attributes
	 * @return confirmedCheckoutScreen.jsp
	 */
	@RequestMapping(value = "/confirmedCheckout", method = RequestMethod.GET)
	public String renderCheckoutComplete(@RequestParam("bookId") String id, @RequestParam("userId") String userid,
			ModelMap model) {	
		System.out.println("BOOK" + id + "USER" + userid);
		Book book = new Book();
		book = (Book) bookService.findById(id);

		Date dueDate = DateUtils.addMonths(new Date(), 1);
		User user = userService.findById(Integer.parseInt(userid));

		model.addAttribute("useremail", getPrincipal());
		model.addAttribute("userid", user.getId());
		model.addAttribute("user", user.getFirstName());
		model.addAttribute("due", dueDate.toString());
		model.addAttribute("book", book);
		return "confirmedCheckoutScreen";
	}

	

	/**
	 * Renders the list of books that are checked out for a given user
	 * @param user Email of the user  
	 * @param model ModelMap to hold the object values
	 * @return checkedOutBooks.jsp
	 */
	@RequestMapping(value = "/viewCheckedOutBooks", method = RequestMethod.GET)
	public String renderCheckedOutBooks(@RequestParam("name") String email, ModelMap model) {

		User current_user = userService.findByEmail(email);
		List<Checkout> checkedOutBooksList = checkoutService.findByUserId(current_user.getId());
		List<Book> books = new ArrayList<Book>();
		for (Checkout checkout : checkedOutBooksList) {
			books.add(checkout.getBook());
		}
		model.addAttribute("books", books);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("useremail", getPrincipal());
		return "checkedOutBooks";
	}

	

	/**
	 * Returns the book with the given id
	 * @param id Id of the book to be returned 
	 * @param username Email of the user
	 * @param model ModelMap to bind the attribute values
	 * @return checkedOutBooks.jsp
	 */
	@RequestMapping(value = "/return-book-{id}", method = RequestMethod.GET)
	public String returnBook(@PathVariable("id") String id, @RequestParam("name") String username, ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(id);
		User user = userService.findByEmail(username);
		Checkout returnCopy = new Checkout();
		List<Checkout> chCopies = checkoutService.findByUserId(user.getId());
		for (Checkout checkout : chCopies) {
			if (checkout.getUserId() == user.getId() && checkout.getBookId() == book.getId()) {
				returnCopy = checkout;
				break;
			}
		}
		System.out.println(returnCopy);
		checkoutService.removeCheckout(returnCopy);
		notificationService.sendReturnMail(returnCopy.getUser(), returnCopy);

		model.addAttribute("val1", "success");
		model.addAttribute("userid", user.getId());
		model.addAttribute("user", user.getFirstName());
		List<Checkout> checkedOutBooks = checkoutService.findByUserId(user.getId());
		List<Book> books = new ArrayList<Book>();
		for (Checkout checkout : checkedOutBooks) {
			books.add(checkout.getBook());
		}
		model.addAttribute("books", books);
		return "checkedOutBooks";
	}

	
}
