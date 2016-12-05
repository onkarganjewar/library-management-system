package edu.sjsu.cmpe275.project.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.Checkout;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 */
@Repository("checkoutDao")
@Transactional
public class CheckoutDaoImpl extends AbstractDao<Serializable, Checkout> implements CheckoutDao{


	@Autowired
	private SessionFactory sessionFactory;

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.project.dao.CheckoutDao#insert(edu.sjsu.cmpe275.project.model.Book, edu.sjsu.cmpe275.project.model.BookCopy, edu.sjsu.cmpe275.project.model.User)
	 */
	@Override
	public void insert(Checkout entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.project.dao.CheckoutDao#remove(edu.sjsu.cmpe275.project.model.Checkout)
	 */
	@Override
	public void remove(Checkout entity) {
		delete(entity);
	}
	
}
