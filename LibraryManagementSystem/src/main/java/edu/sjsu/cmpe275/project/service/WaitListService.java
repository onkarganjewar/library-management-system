package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */
public interface WaitListService {

	public void addRecord(WaitList entity);
	public void deleteRecordById(Integer waitListId);
	public List<User> findAllUsersForBook (Integer bookId);
	public List<Book> findAllBooksForUser (Integer userId);
	public WaitList getFirstInLineForBook (Integer bookId);
	public void removeUserFromWaitList (Integer userId, Integer bookId);
	public WaitList getRecordByWaitListId (Integer waitListId);
	public List<WaitList> findAllRecords ();
	public void updateRecord(WaitList record);
}
