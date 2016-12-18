package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
public interface CheckoutService {
	void addCheckout(Checkout entity);
	void removeCheckout(Checkout entity);
	List<Checkout> findByBookId (int bookId);
	List<Checkout> findByUserId (int userId);
	void updateCheckout(Checkout entity);
	List<Checkout> findAllRecords();
}
