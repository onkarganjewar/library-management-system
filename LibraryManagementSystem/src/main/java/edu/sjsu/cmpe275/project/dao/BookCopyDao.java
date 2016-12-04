/**
 * 
 */
package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

/**
 * @author Onkar Ganjewar
 * 
 */
public interface BookCopyDao {
	void save(BookCopy bookCopy);
	BookCopy findByBook (Book book);
}
