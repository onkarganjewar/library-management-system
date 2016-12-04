package edu.sjsu.cmpe275.project.dao;


import java.util.List;

import edu.sjsu.cmpe275.project.model.Book;

/**
 * @author Onkar Ganjewar
 */
public interface BookDao {
	void save(Book book);
	List<Book> findByTitle(String title);
	void deleteById(int id);
	List<Book> findAllBooks();
	Book findbyId(String id);
	void modify(Book book);
}
