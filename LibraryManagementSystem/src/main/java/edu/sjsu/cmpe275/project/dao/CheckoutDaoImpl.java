package edu.sjsu.cmpe275.project.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
@Repository("checkoutDao")
@Transactional
public class CheckoutDaoImpl extends AbstractDao<Serializable, Checkout> implements CheckoutDao{

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insert(Checkout entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void remove(Checkout entity) {
		delete(entity);
	}

	@Override
	public List<Checkout> findByBookId(int bookId) {
		logger.info("Book Id : {}", bookId);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("bookId", bookId));
		List<Checkout> checkoutList = (List<Checkout>) crit.list();
		if(checkoutList!=null && checkoutList.size()>0){
			for (Checkout checkout : checkoutList) {
				Hibernate.initialize(checkout.getCopy());	
			}
		}
		return checkoutList;
	}

	@Override
	public List<Checkout> findByUserId(int userId) {
		logger.info("User Id : {}", userId);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("userId", userId));
		List<Checkout> checkoutList = (List<Checkout>) crit.list();
		if(checkoutList!=null && checkoutList.size()>0){
			for (Checkout checkout : checkoutList) {
				Hibernate.initialize(checkout.getCopy());
				Hibernate.initialize(checkout.getCopy().getBooks());
			}
		}
		return checkoutList;
	}

	@Override
	public void modify(Checkout entity) {
		// TODO Auto-generated method stub
		update(entity);
	}

	@Override
	public List<Checkout> findAllCopies() {
		return sessionFactory.getCurrentSession().createCriteria(Checkout.class).list();
	}
}
