/**
 * 
 */
package edu.sjsu.cmpe275.project.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.CheckoutDao;
import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
@Service("checkoutService")
@Transactional
public class CheckoutServiceImpl implements CheckoutService{

	@Autowired
	private CheckoutDao checkoutDao;

	@Override
	public void addCheckout(Checkout entity) {
		checkoutDao.insert(entity);
	}

	@Override
	public void removeCheckout(Checkout entity) {
		checkoutDao.remove(entity);
	}

	@Override
	public List<Checkout> findByBookId(int bookId) {
		return checkoutDao.findByBookId(bookId);
	}

	@Override
	public List<Checkout> findByUserId(int userId) {
		return checkoutDao.findByUserId(userId);
	}

	@Override
	public void updateCheckout(Checkout entity) {
		// TODO Auto-generated method stub
		checkoutDao.modify(entity);
		
	}

	@Override
	public List<Checkout> findAllRecords() {
		return checkoutDao.findAllCopies();
	}
	
}
