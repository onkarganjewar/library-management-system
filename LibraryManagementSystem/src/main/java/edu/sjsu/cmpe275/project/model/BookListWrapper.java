/**
 * 
 */
package edu.sjsu.cmpe275.project.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Onkar Ganjewar
 * 
 */
public class BookListWrapper {
	public List<Book> booksList ;

	public BookListWrapper() {
		this.booksList = new ArrayList<Book>();	
	}

	public List<Book> getBooksList() {
		return booksList;
	}

	public void setBooksList(List<Book> booksList) {
		this.booksList = booksList;
	}

	@Override
	public String toString() {
		return "BookListWrapper [booksList=" + booksList + "]";
	}
	
}
