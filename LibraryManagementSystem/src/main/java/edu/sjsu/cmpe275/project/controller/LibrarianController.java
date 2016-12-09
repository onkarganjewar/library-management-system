/**
 * 
 */
package edu.sjsu.cmpe275.project.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.service.BookCopyService;
import edu.sjsu.cmpe275.project.service.BookService;
import edu.sjsu.cmpe275.project.service.UserService;

/**
 * @author Onkar Ganjewar
 */

@Controller
@RequestMapping("/librarian")
public class LibrarianController {

	@Autowired
	private BookCopyService bookCopyService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;


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
		// TODO: Handle the condition if the user is anonymousUser
		User currentuser = userService.findByEmail(email_user);
		model.addAttribute("user", currentuser.getFirstName());

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
	public String registerNewBook(Book book, @ModelAttribute("copies") String copy) {

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
		return "redirect:/admin";
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


}
