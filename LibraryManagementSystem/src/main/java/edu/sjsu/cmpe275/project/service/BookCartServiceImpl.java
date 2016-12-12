package edu.sjsu.cmpe275.project.service;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.BookCartDao;
import edu.sjsu.cmpe275.project.model.BookCart;

/**
 * @author Onkar Ganjewar
 */

@Service("bookCartService")
@Transactional
public class BookCartServiceImpl implements BookCartService{

	@Autowired
	private BookCartDao bookCartDao;
	
	@Override
	public void addToCart(BookCart entity) {
		bookCartDao.insert(entity);
	}

	@Override
	public void removeFromCart(int userId, int bookId) {
		List<BookCart> bookCartList = bookCartDao.findByUserId(userId);
		for (BookCart bookCart : bookCartList) {
			if (bookCart.getBookId() == bookId)
				bookCartDao.remove(bookCart);
		}
	}

	@Override
	public List<BookCart> findByUserId(int userId) {
		return bookCartDao.findByUserId(userId);
	}
}
