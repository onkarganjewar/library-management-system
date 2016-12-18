package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.BooksHoldList;


/**
 * @author Onkar Ganjewar
 */
public interface HoldListDao {

	public BooksHoldList findByBookCopyId (Integer copyId);
	public BooksHoldList findByHoldListId (Integer holdListId);
	public void insert(BooksHoldList entity);
	public void remove(BooksHoldList entity);
	public void modify(BooksHoldList entity);
	public void removeByHoldListId (Integer holdListId);
	public List<BooksHoldList> findByBookId (Integer bookId);
	public List<BooksHoldList> findByUserId (Integer userId);
	public List<BooksHoldList> findAllRecords ();
}
