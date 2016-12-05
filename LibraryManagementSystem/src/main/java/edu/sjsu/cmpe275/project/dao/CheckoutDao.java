package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
public interface CheckoutDao {

	void insert(Checkout entity);
	void remove(Checkout entity);
	
}
