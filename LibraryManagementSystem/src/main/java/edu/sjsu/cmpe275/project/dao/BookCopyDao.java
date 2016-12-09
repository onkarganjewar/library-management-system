package edu.sjsu.cmpe275.project.dao;

import java.util.List; 

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

/**
 * @author Onkar Ganjewar
 */
public interface BookCopyDao {
	void save(BookCopy bookCopy);
	List<BookCopy> findByBook (Book book);
	void remove(BookCopy bookCopy);
}
