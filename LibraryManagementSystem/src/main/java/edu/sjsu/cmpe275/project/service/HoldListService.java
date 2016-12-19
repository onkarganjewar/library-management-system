package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.BooksHoldList;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 */
public interface HoldListService {
	public void addRecord(BooksHoldList entity);
	public void deleteRecordById(Integer holdListId);
	public List<User> findAllUsersForBook (Integer bookId);
	public List<Book> findAllBooksForUser (Integer userId);
	public BooksHoldList getFirstInLineForBook (Integer bookId);
	public void removeUserFromHoldList (Integer userId, Integer bookId);
	public BooksHoldList getRecordByHoldListId (Integer waitListId);
	public List<BooksHoldList> findAllRecords ();
	public void updateRecord(BooksHoldList record);
	public List<BookCopy> findAllBookCopies (Integer bookId);
  	public List<BooksHoldList> findAllRecordsByBookId (Integer bookId);

}
