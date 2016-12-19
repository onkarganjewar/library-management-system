package edu.sjsu.cmpe275.project.controller;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


import org.apache.commons.lang.time.DateUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

import edu.sjsu.cmpe275.project.dao.MyCalendarDao;
import edu.sjsu.cmpe275.project.dao.UserDaoImpl;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCart;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.BookListWrapper;
import edu.sjsu.cmpe275.project.model.BooksHoldList;
import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.MyCalendar;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.WaitList;
import edu.sjsu.cmpe275.project.service.BookCartService;
import edu.sjsu.cmpe275.project.service.BookCopyService;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.CheckoutService;
import edu.sjsu.cmpe275.project.service.HoldListService;
import edu.sjsu.cmpe275.project.service.NotificationService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.service.WaitListService;
import edu.sjsu.cmpe275.project.util.CustomTimeService;

/**
 * @author Onkar Ganjewar
 */

@Controller
@RequestMapping("/patron")
public class PatronController {

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private MyCalendarDao myCalendarDao;

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private BookCartService cartService;

	@Autowired
	private WaitListService waitListService;

	@Autowired
	private BookService bookService;

	@Autowired
	private BookCopyService bookCopyService;

	@Autowired
	private UserService userService;

	@Autowired
	private HoldListService holdListService;

	@Autowired
	private CustomTimeService myTimeService;

	/**
	 * Renders the home page for the patron.
	 * 
	 * @param model
	 *            ModelMap to hold the object data.
	 * @return users.jsp
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String renderIndex(ModelMap model) {
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("useremail", getPrincipal());
		model.addAttribute("userid", currentuser.getId());
		System.out.println("GET DATE: "+myTimeService.getDate());
		model.addAttribute("fine", currentuser.getFine());
		return "usersHome";
	}
	
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public void renderAlerts() {
		System.out.println(myTimeService.getDate());
//		Calendar myCal = Calendar.getInstance();
//		myCal.set(Calendar.YEAR, 2016);
//		myCal.set(Calendar.MONTH, 11);
//		myCal.set(Calendar.DAY_OF_MONTH, 28);
//		Date customDate = myCal.getTime();
//		myTimeService.setDate(customDate);
//		System.out.println(myTimeService.getDate());
	}
	

	@RequestMapping(value = "/confirm-checkout", method = RequestMethod.POST)
	public String renderVerifyCheckout(@ModelAttribute("bookListWrapper") BookListWrapper fooListWrapper,
			@RequestParam("name") String userid, Model model) {

		List<Book> selectedBooks = new ArrayList<Book>();
		List<Book> returnedIds = fooListWrapper.getBooksList();
		for (Book book : returnedIds) {
			if (book.getId() != null)
				selectedBooks.add(bookService.findById(book.getId().toString()));
		}
		User user = userService.findById(Integer.parseInt(userid));
//		Date dueDate = DateUtils.addMonths(new Date(), 1);
		Date dueDate = DateUtils.addMonths(myTimeService.getDate(), 1);
		
		BookListWrapper bookListWrapper = new BookListWrapper();
		bookListWrapper.setBooksList(selectedBooks);
		model.addAttribute("bookListWrapper", bookListWrapper);
		model.addAttribute("due", dueDate);
		model.addAttribute("userid", Integer.parseInt(userid));
		model.addAttribute("user", user.getFirstName());
		logger.info(fooListWrapper.toString());
		return "ConfirmCheckoutBooks";
	}

	/**
	 * Renders the retrieved results for the searched book by the title
	 * 
	 * @param txtSearch
	 *            Title of the book to be searched
	 * @param model
	 *            ModelMap to hold the book object data
	 * @return users.jsp
	 */
	@RequestMapping(value = "/search-book-{txtSearch}", method = RequestMethod.GET)
	public String renderSearchedBooks(@PathVariable String txtSearch, ModelMap model) {
		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("books", books);
		BookListWrapper bookListWrapper = new BookListWrapper();
		bookListWrapper.setBooksList(books);
		model.addAttribute("bookListWrapper", bookListWrapper);
		model.addAttribute("userid", currentuser.getId());
//		System.out.println("GET DATE: "+ myTimeService.getDate());
		return "userSearchResults";
	}

	/**
	 * Renders the book checkout confirmation form
	 * 
	 * @param id
	 *            Id of the book to be checked out
	 * @param username
	 *            Name/Email of the user
	 * @param model
	 *            ModelMap to hold the book/user object data
	 * @return bookCheckoutWindow.jsp
	 */
	@RequestMapping(value = "/checkout-book-{id}", method = RequestMethod.GET)
	public String renderCheckoutConfirmation(@PathVariable("id") String id, @RequestParam("name") String username,
			ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(id);

//		Date dueDate = DateUtils.addMonths(new Date(), 1);
		Date dueDate = DateUtils.addMonths(myTimeService.getDate(), 1);
		User user = userService.findByEmail(username);

		model.addAttribute("userid", user.getId());
		model.addAttribute("user", user.getFirstName());
		model.addAttribute("due", dueDate.toString());
		model.addAttribute("book", book);
		return "bookCheckoutWindow";
	}

	/**
	 * Checks out the particular book for the given user.
	 * 
	 * @param id
	 *            Id of the book to be checked out
	 * @param userid
	 *            Id of the user that wants the book
	 * @return Success, if the book is checked out successfully <br>
	 *         Failure, if there's any error in issuing of the book </br>
	 *         Duplicate, if the book is already checked out
	 */
	@RequestMapping(value = "/confirm-checkout-book-{id}", method = RequestMethod.GET)
	public @ResponseBody String checkoutBook(@PathVariable("id") String id, @RequestParam("name") String userid) {
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
//		Date currentDate = new Date();
		Date currentDate = myTimeService.getDate();
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
		checkout.setTimes(0);
		List<Checkout> checkoutCopies = new ArrayList<Checkout>();
		checkoutCopies.add(checkout);
		book.setCheckoutCopies(checkoutCopies);
		user.setCheckoutCopies(checkoutCopies);

		try {
			checkoutService.addCheckout(checkout);
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
	 * 
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
	 * 
	 * @param id
	 *            Id of the book checked out
	 * @param userid
	 *            Id of the user
	 * @param model
	 *            ModelMap to hold the book/user attributes
	 * @return confirmedCheckoutScreen.jsp
	 */
	@RequestMapping(value = "/confirmedCheckout", method = RequestMethod.GET)
	public String renderCheckoutComplete(@RequestParam("bookId") String id, @RequestParam("userId") String userid,
			ModelMap model) {
		System.out.println("BOOK" + id + "USER" + userid);
		Book book = new Book();
		book = (Book) bookService.findById(id);

//		Date dueDate = DateUtils.addMonths(new Date(), 1);
		Date dueDate = DateUtils.addMonths(myTimeService.getDate(), 1);
		
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
	 * 
	 * @param user
	 *            Email of the user
	 * @param model
	 *            ModelMap to hold the object values
	 * @return checkedOutBooks.jsp
	 */
	@RequestMapping(value = "/viewCheckedOutBooks", method = RequestMethod.GET)
	public String renderCheckedOutBooks(@RequestParam("name") String userId, ModelMap model) {

		User currentuser = userService.findById(Integer.parseInt(userId));
		List<Checkout> checkedOutBooksList = checkoutService.findByUserId(Integer.parseInt(userId));
		List<Book> books = new ArrayList<Book>();
		for (Checkout checkout : checkedOutBooksList) {
			books.add(checkout.getBook());
		}
		model.addAttribute("books", books);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("useremail", currentuser.getEmail());
		return "checkedOutBooks";
	}

	/**
	 * Returns the book with the given id
	 * 
	 * @param id
	 *            Id of the book to be returned
	 * @param username
	 *            Email of the user
	 * @param model
	 *            ModelMap to bind the attribute values
	 * @return checkedOutBooks.jsp
	 */
	@RequestMapping(value = "/return-book-{id}", method = RequestMethod.GET)
	public String returnBook(@PathVariable("id") String id, @RequestParam("name") String username, ModelMap model) {
		Book book = new Book();
		boolean waitListExists = true;
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
		WaitList firstInLine = null;

		try {
			firstInLine = waitListService.getFirstInLineForBook(book.getId());
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
			waitListExists = false;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		if (waitListExists) {
			firstInLine.setDateAssigned(myTimeService.getDate());
			waitListService.deleteRecordById(firstInLine.getWaitListId());
			BooksHoldList holdList = new BooksHoldList();
			holdList.setBook(firstInLine.getBook());
			holdList.setBookCopyId(returnCopy.getCopy().getId());
			holdList.setBookId(firstInLine.getBookId());
			holdList.setBookCopy(returnCopy.getCopy());
			holdList.setDateAssigned(firstInLine.getDateAssigned());
			holdList.setUser(firstInLine.getUser());
			holdList.setUserId(firstInLine.getUserId());
			holdListService.addRecord(holdList);
			Checkout waitCheck = new Checkout();
			waitCheck.setBook(firstInLine.getBook());
//			waitCheck.setBookId(firstInLine.getBookId());
			waitCheck.setUser(firstInLine.getUser());
			notificationService.sendBookAvailableMail(waitCheck, 3);
		}

		book.setAvailability("Available");
		bookService.updateBook(book);
		notificationService.sendReturnMail(returnCopy.getUser(), returnCopy);

		model.addAttribute("val1", "success");
		model.addAttribute("userid", user.getId());
		model.addAttribute("user", user.getFirstName());
		List<Checkout> checkedOutBooks = checkoutService.findByUserId(user.getId());
		List<Book> books = new ArrayList<Book>();
		for (Checkout checkout : checkedOutBooks) {
			books.add(checkout.getBook());
		}

		model.addAttribute("useremail", getPrincipal());
		model.addAttribute("books", books);
		return "checkedOutBooks";
	}

	@RequestMapping(value = "/addToCart-{id}", method = RequestMethod.GET)
	public @ResponseBody String addToCart(@PathVariable("id") String id, @RequestParam("name") String userid,
			ModelMap model) {
		int userId = Integer.parseInt(userid);
		int bookId = Integer.parseInt(id);
		boolean onHold = false;
		List<Checkout> checkedOutCopies = checkoutService.findByBookId(bookId);
		List<BookCopy> bookCopies = bookCopyService.findAllByBook(bookService.findById(id));
//		List<User> usersList =	holdListService.findAllUsersForBook(bookId);
		List<BookCopy> holdCopies = holdListService.findAllBookCopies(bookId);
		BooksHoldList firstHold = null;
		try {
			firstHold = holdListService.getFirstInLineForBook(bookId);
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		if (firstHold != null) {
			if (firstHold.getUserId().equals(userId)) {
				onHold = true;
			}
		}
			
		// Check whether the user holds the different copy of the given book
		List<Checkout> checkoutUsersList = checkoutService.findByUserId(userId);
		if (checkoutUsersList != null && !checkoutUsersList.isEmpty()) {
			for (Checkout checkout : checkoutUsersList) {
				if (checkout.getBookId() == bookId) {
					return "AlreadyCheckedOut";
				}
			}
		}
		
		int outCopies = (checkedOutCopies.size() + holdCopies.size());
		int allCopies = bookCopies.size();
		if (allCopies == checkedOutCopies.size() && (!onHold) )
			return "Unavailable";
		if (allCopies == outCopies && (!onHold))
			return "OnHold";

		BookCart entity = new BookCart();
		entity.setBookId(bookId);
		entity.setUserId(userId);

		try {
			cartService.addToCart(entity);
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				return "AlreadyInCart";
			}
			return "Failure";
		}
		return "Success";
	}

	@RequestMapping(value = "/viewCart", method = RequestMethod.GET)
	public String renderCart(@RequestParam("name") String userid, ModelMap model) {

		User current_user = userService.findById(Integer.parseInt(userid));
		List<BookCart> cartItems = cartService.findByUserId(current_user.getId());
		List<Book> cartBooks = new ArrayList<Book>();
		for (BookCart bookCart : cartItems) {
			cartBooks.add(bookCart.getBook());
		}
		BookListWrapper bookListWrapper = new BookListWrapper();
		bookListWrapper.setBooksList(cartBooks);
		model.addAttribute("bookListWrapper", bookListWrapper);
		model.addAttribute("user", current_user.getFirstName());
		model.addAttribute("useremail", getPrincipal());
		model.addAttribute("userid", current_user.getId());
		return "BookCart";
	}

	@RequestMapping(value = "/cart-search-book-{txtSearch}", method = RequestMethod.GET)
	public String renderSearchedBooks(@PathVariable String txtSearch, @RequestParam("name") String id, ModelMap model) {
		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);
		User currentuser = userService.findById(Integer.parseInt(id));
		// User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("books", books);
		List<BookCart> cartItems = cartService.findByUserId(Integer.parseInt(id));
		List<Book> cartBooks = new ArrayList<Book>();
		for (BookCart bookCart : cartItems) {
			cartBooks.add(bookCart.getBook());
		}
		books.removeAll(cartBooks);

		BookListWrapper bookListWrapper = new BookListWrapper();
		bookListWrapper.setBooksList(books);
		model.addAttribute("bookListWrapper", bookListWrapper);
		model.addAttribute("userid", currentuser.getId());
		return "userSearchResults";
	}

	@RequestMapping(value = "/remove-from-cart-{id}", method = RequestMethod.GET)
	public String removeBookFromCart(@PathVariable("id") String id, @RequestParam("name") String userid,
			ModelMap model) {
		Book book = new Book();
		book = (Book) bookService.findById(id);
		User user = userService.findById(Integer.parseInt(userid));
		BookCart removeCopy = new BookCart();
		List<BookCart> rmCopies = cartService.findByUserId(Integer.parseInt(userid));
		for (BookCart bookCart : rmCopies) {
			if (bookCart.getUserId() == user.getId() && bookCart.getBookId() == book.getId()) {
				removeCopy = bookCart;
				break;
			}
		}

		System.out.println(removeCopy);
		cartService.removeFromCart(Integer.parseInt(userid), Integer.parseInt(id));
		List<BookCart> cartItems = cartService.findByUserId(Integer.parseInt(userid));
		List<Book> cartBooks = new ArrayList<Book>();
		for (BookCart bookCart : cartItems) {
			cartBooks.add(bookCart.getBook());
		}
		BookListWrapper bookListWrapper = new BookListWrapper();
		bookListWrapper.setBooksList(cartBooks);
		model.addAttribute("bookListWrapper", bookListWrapper);
		model.addAttribute("user", user.getFirstName());
		model.addAttribute("useremail", getPrincipal());
		model.addAttribute("val1", "success");
		model.addAttribute("userid", user.getId());
		return "BookCart";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutBooks(@ModelAttribute("bookListWrapper") BookListWrapper fooListWrapper, BindingResult error,
			@RequestParam("name") String userId, ModelMap model) {
		System.out.println(userId);
		List<Book> selectedBooks = new ArrayList<Book>();
		List<Book> returnedIds = fooListWrapper.getBooksList();
		List<Integer> bookIdList = new ArrayList<Integer>();

		for (Book book : returnedIds) {
			if (book.getId() != null)
				bookIdList.add(book.getId());
			selectedBooks.add(bookService.findById(book.getId().toString()));
		}
		User user = userService.findById(Integer.parseInt(userId));
		String returnVal = "";
		String returnTitle = "";
		for (Integer i : bookIdList) {
			if (returnVal.equals("Failure") || returnVal.equals("BookCopyNotAvailable") || returnVal.equals("Duplicate")
					|| returnVal.equals("OverallCheckoutLimitReached") || returnVal.equals("DayCheckoutLimitReached")) {
				Book returnBook = bookService.findById(i.toString());
				returnTitle = returnBook.getTitle();
				break;
			}
			returnVal = checkoutBook(Integer.parseInt(userId), i);
		}

		model.addAttribute("val1", returnVal);
		model.addAttribute("bookTitle", returnTitle);
		model.addAttribute("books", selectedBooks);

//		Date dueDate = DateUtils.addMonths(new Date(), 1);
		Date dueDate = DateUtils.addMonths(myTimeService.getDate(), 1);

		
		model.addAttribute("due", dueDate.toString());

		model.addAttribute("userid", user.getId());

		return "confirmedCheckout";

	}

	private String checkoutBook(Integer userId, Integer bookId) {
		boolean lastCopy = false;
		List<BookCopy> checkoutBookCopiesList = new ArrayList<BookCopy>();

		// Get the book from the book id
		Book book = bookService.findById(Integer.toString(bookId));

		// Get the list of all the copies for the given book
		List<BookCopy> allBookCopies = bookCopyService.findAllByBook(book);

		// Get the list of checked out copies for the given book
		List<Checkout> checkoutBooksList = checkoutService.findByBookId(bookId);

		if (allBookCopies.size() == 1)
			lastCopy = true;
		
		List<BooksHoldList> holdListArr = holdListService.findAllRecordsByBookId(bookId);
		for (BooksHoldList booksHoldList : holdListArr) {
			if (booksHoldList.getUserId().equals(userId))
				holdListService.deleteRecordById(booksHoldList.getHoldListId());
		}

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
				return "BookCopyNotAvailable";
			} else if (allBookCopies.size() == (checkoutBooksList.size() + 1)) {
				lastCopy = true;
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
				return "OverallCheckoutLimitReached";
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
//		Date currentDate = new Date();
		Date currentDate = myTimeService.getDate();
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
			return "DayCheckoutLimitReached";

		// Insert the new checkout record in the database
		Checkout checkout = new Checkout();
		checkout.setBook(book);
		checkout.setCopy(diffCopies.get(0));
		checkout.setCheckoutDate(currentDate);
		checkout.setUser(user);
		checkout.setBookId(bookId);
		checkout.setUserId(userId);
		checkout.setTimes(0);
		List<Checkout> checkoutCopies = new ArrayList<Checkout>();
		checkoutCopies.add(checkout);
		book.setCheckoutCopies(checkoutCopies);
		user.setCheckoutCopies(checkoutCopies);
		try {
			checkoutService.addCheckout(checkout);
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				return "Duplicate";
			}
			return "Failure";
		}
		cartService.removeFromCart(userId, bookId);

		notificationService.sendCheckoutMail(checkout.getUser(), checkout);
		// Update the respective book and user entities to
		// avoid making them transient
		if (lastCopy)
			book.setAvailability("Not Available");
		bookService.updateBook(book);
		userService.updateUser(user);
		return "Success";
	}

	@RequestMapping(value = "/renew-book-{id}", method = RequestMethod.GET)
	public String renewBook(@PathVariable("id") String id, @RequestParam("name") String username, ModelMap model) {
		Book book = new Book();
		boolean waitListExists=false;
		Integer bookId=Integer.parseInt(id);
		book = (Book) bookService.findById(id);
		User user = userService.findByEmail(username);
		Checkout renewCopy = new Checkout();
		List<Checkout> chCopies = checkoutService.findByUserId(user.getId());
		for (Checkout checkout : chCopies) {
			if (checkout.getUserId() == user.getId() && checkout.getBookId() == book.getId()) {
				renewCopy = checkout;
				break;
			}
		}
		List<User> waitList=waitListService.findAllUsersForBook(bookId);
		if(!waitList.isEmpty()){
			waitListExists = true;
			model.addAttribute("val2", "waitListExists");

		}
		else{
			System.out.println(renewCopy);
			Integer freq = renewCopy.getTimes();
			if (freq >= 2) {
				model.addAttribute("val2", "exceeded");
			} else {
				Date oldCheckoutDate = renewCopy.getCheckoutDate();
				Date updatedCheckoutDate = DateUtils.addMonths(oldCheckoutDate, 1);
				freq = freq + 1;
				renewCopy.setTimes(freq);
				renewCopy.setCheckoutDate(updatedCheckoutDate);
				checkoutService.updateCheckout(renewCopy);
				model.addAttribute("val2", "RenewalSuccess");
			}
		}

		model.addAttribute("userid", user.getId());
		model.addAttribute("useremail", user.getEmail());
		model.addAttribute("user", user.getFirstName());
		List<Checkout> checkedOutBooks = checkoutService.findByUserId(user.getId());
		List<Book> books = new ArrayList<Book>();
		for (Checkout checkout : checkedOutBooks) {
			books.add(checkout.getBook());
		}

		model.addAttribute("books", books);
		return "checkedOutBooks";
	}

	//
	@RequestMapping(value = "/add-to-waiting-list-{id}", method = RequestMethod.GET)
	public @ResponseBody String addToWaitingList(@PathVariable("id") String id, @RequestParam("name") String userId,
			ModelMap model) {
		Integer userid = Integer.parseInt(userId);
		Integer bookId = Integer.parseInt(id);
		Book book = new Book();
		book = (Book) bookService.findById(id);
		User user = userService.findById(userid);

		WaitList waitList = new WaitList();
		waitList.setBookId(bookId);
		waitList.setBook(book);
		waitList.setDateAdded(myTimeService.getDate());
		waitList.setUser(user);
		waitList.setUserId(userid);
		try {
			waitListService.addRecord(waitList);
		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				return "Failed";

			}
			return "Failed";
		}
		return "Added";
	}

}
