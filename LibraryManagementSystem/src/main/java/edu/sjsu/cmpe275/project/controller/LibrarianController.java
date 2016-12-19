package edu.sjsu.cmpe275.project.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.hibernate.service.spi.ServiceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sjsu.cmpe275.project.configuration.AppConfig;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.AlertService;
import edu.sjsu.cmpe275.project.service.BookCopyService;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.UserService;
import edu.sjsu.cmpe275.project.util.CustomTimeService;

/**
 * @author Onkar Ganjewar
 */
@Controller
@RequestMapping("/librarian")
public class LibrarianController {

	static final Logger logger = LoggerFactory.getLogger(LibrarianController.class);

	@Autowired
	private AlertService alertService;

	@Autowired
	private BookCopyService bookCopyService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@Autowired
	private CustomTimeService myTimeService;


	/**
	 * Renders the home page for the librarian.
	 * @param model ModelMap to hold the object data.
	 * @return admin.jsp
	 */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String renderIndex(ModelMap model) {
		List<Book> books = bookService.findAllBooks();
		model.addAttribute("books", books);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("dateTime",myTimeService.getDate());
		return "admin";
	}

	/**
	 * Renders the book registration page for the librarian.
	 * @param model ModelMap holding the book data.
	 * @return
	 * 			newBook.jsp
	 */
	@RequestMapping(value = "/new-book", method = RequestMethod.GET)
	public String renderBookRegistration(ModelMap model) {
		Book books = new Book();
		model.addAttribute("book", books);
		Calendar myCal = Calendar.getInstance();
		myCal.set(Calendar.YEAR, 2016);
		myCal.set(Calendar.MONTH, 11);
		myCal.set(Calendar.DAY_OF_MONTH, 28);
		Date customDate = myCal.getTime();
		myTimeService.setDate(customDate);
		System.out.println(myTimeService.getDate());
		return "newBook";
	}

	/**
	 * Renders the search results based on the given book title
	 * @param txtSearch Title of the book to be searched
	 * @param model ModelMap to bind the book object attributes
	 * @return searchResults.jsp
	 */
	@RequestMapping(value = "/search-book-{txtSearch:.+}", method = RequestMethod.GET)
	public String renderSearchResults(@PathVariable String txtSearch, ModelMap model) {
		List<Book> books = (List<Book>) bookService.findByTitle(txtSearch);

		model.addAttribute("books", books);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("dateTime",myTimeService.getDate());

		return "searchResults";
	}

	
	/**
	 * Stores the given book entity.
	 * 
	 * @param book
	 * 				Book model
	 * @param copy
	 * 				ModelAttribute "copies"
	 * @return
	 */
	@RequestMapping(value = { "/new-book" }, method = RequestMethod.POST)
	public String registerNewBook(Book book,BindingResult result, ModelMap model, @ModelAttribute("copies") String copy) {

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
				bookCopyService.saveCopy(bookCopy);
			}
		} else {
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(returnBook);
			bookCopyService.saveCopy(bookCopy);
		}
		return "redirect:/librarian/home";
	}

	/**
	 * Renders the book modification form.
	 * @param id PathVariable containing the id of the book to be modified
	 * @param model ModelMap to hold the book object data
	 * @return	editBook.jsp
	 */
	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.GET)
	public String renderBookEdit(@PathVariable String id, ModelMap model) {
		Book book = bookService.findById(id);
		int copies = 1;
		List<BookCopy> returnCopies = bookCopyService.findAllByBook(book);
		copies = returnCopies.size();
		model.addAttribute("book", book);
		model.addAttribute("copies", copies);
		model.addAttribute("edit", true);
		return "editBook";
	}
	
	
	/**
	 * Updates the given book.
	 * @param book Book object to be updated
	 * @param id Id of the book
	 * @param cn No of copies for the book
	 * @return
	 */
	@RequestMapping(value = { "/edit-book-{id}" }, method = RequestMethod.POST)
	public String updateBook(Book book, @PathVariable String id,
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
		Book returnBook = bookService.findById(id);

		if (!NaN) {
			// If a valid number then create the specified number of copies
			// get the number of copies existing for that book
			List<BookCopy> copiesList = bookCopyService.findAllByBook(book);
			for (BookCopy bookCopy : copiesList) {
				bookCopyService.deleteCopy(bookCopy);
			}
			for (int i = 0; i < copies; i++) {
				BookCopy bookCopy = new BookCopy();
				bookCopy.setBooks(returnBook);
				bookCopyService.saveCopy(bookCopy);
			}
		} else {
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(returnBook);
			bookCopyService.saveCopy(bookCopy);
		}

		return "redirect:/librarian/home";
	}

	/**
	 * Deletes the book.
	 * @param id Id of the book to be deleted
	 * @param mo ModelMap holding the book attributes
	 * @return admin.jsp
	 */
	@RequestMapping(value = { "/delete-book-{id}" }, method = RequestMethod.GET)
	public String deleteBook(@PathVariable String id, ModelMap mo) {

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
		List<Book> books = bookService.findAllBooks();
		mo.addAttribute("books", books);
		String email_user = getPrincipal();
		User currentuser = userService.findByEmail(email_user);
		mo.addAttribute("user", currentuser.getFirstName());
		return "admin";
	}

	
	@RequestMapping(value = { "/delete-book-search-{id}" }, method = RequestMethod.GET)
	public String deleteBookFromSearch(@PathVariable String id, ModelMap mo) {
		Book book = bookService.findById(id);
		String bookTitle=book.getTitle();
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
	 * Renders the book registration page and populates the fields using ISBN
	 * @param isbn ISBN to search the book from
	 * @param model ModelMap to bind the book object attributes
	 * @return newBook.jsp
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/bookInfo-{isbn}", method = RequestMethod.GET)
	public String renderBookByISBN(@PathVariable String isbn, ModelMap model) throws MalformedURLException, IOException {
		// String isbn = "0201633612";
		// @RequestParam("isbn") String isbn
		URL url;
		url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);

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

	/**
	 * Register a book by the ISBN values
	 * @param book Book entity to be registered
	 * @param copy No of copies to register
	 * @return redirects to the admin home page
	 */
	@RequestMapping(value = { "/bookInfo-{isbn}" }, method = RequestMethod.POST)
	public String saveBookByISBN(Book book, BindingResult result, @ModelAttribute("copies") String copy) {

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
				bookCopyService.saveCopy(bookCopy);
			}
		} else {
			BookCopy bookCopy = new BookCopy();
			bookCopy.setBooks(returnBook);
			bookCopyService.saveCopy(bookCopy);
		}
		return "redirect:/librarian/home";
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
	
	@RequestMapping(value = "/custom-time-{timeDate}", method = RequestMethod.GET)
	public String customTimer(@PathVariable String timeDate,ModelMap model) {
		
		Date customDate = new Date(timeDate);
		myTimeService.setDate(customDate);
		List<Book> books = bookService.findAllBooks();
		model.addAttribute("books", books);
		String email_user = getPrincipal();
		triggerAlerts(customDate);
		// TODO: Handle the condition if the user is anonymousUser
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());
		model.addAttribute("dateTime",myTimeService.getDate());
		return "admin";
	}

	/**
	 * Start the alert service to check for all the due dates and checkout dates
	 * @param customDate Test date to compare the values against
	 */
	private void triggerAlerts(Date customDate) {
		logger.info("Alert service triggered");
		try {
			alertService.sendAlerts(customDate, true);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("Something went wrong");
		}

		try {
			alertService.generateFines(customDate, true);
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.error("Something went wrong");
		}
	}


}
