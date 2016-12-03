/**
 * 
 */
package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Book;

/**
 * @author Onkar Ganjewar
 * 
 */
public interface BookDao {
	void save(Book book);
	Book findByTitle(String title);
	void deleteById(int id);
}
