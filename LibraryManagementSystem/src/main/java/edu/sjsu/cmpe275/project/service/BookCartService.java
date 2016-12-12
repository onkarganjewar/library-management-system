package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.BookCart;

/**
 * @author Onkar Ganjewar
 */
public interface BookCartService {
	void addToCart(BookCart entity);
	void removeFromCart(int userId, int bookId);
	List<BookCart> findByUserId (int userId);
}
