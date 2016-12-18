package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
public interface CheckoutDao {

	void insert(Checkout entity);
	void remove(Checkout entity);
	List<Checkout> findByBookId (int bookId);
	List<Checkout> findByUserId (int userId);
	void modify(Checkout entity);
	List<Checkout> findAllCopies ();
}
