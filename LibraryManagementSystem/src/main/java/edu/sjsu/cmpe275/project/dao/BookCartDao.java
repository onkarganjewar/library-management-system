package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.BookCart;


/**
 * @author Onkar Ganjewar
 */

public interface BookCartDao {
	void insert(BookCart entity);
	void remove(BookCart entity);
//	List<BookCart> findByBookId (int bookId);
	List<BookCart> findByUserId (int userId);
}
