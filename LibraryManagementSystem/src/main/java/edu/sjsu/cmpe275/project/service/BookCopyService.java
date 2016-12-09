package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

/**
 * @author Onkar Ganjewar
 */
public interface BookCopyService {
	void saveCopy (BookCopy copy);
	List<BookCopy> findAllByBook (Book book);
	void deleteCopy (BookCopy copy);
}
