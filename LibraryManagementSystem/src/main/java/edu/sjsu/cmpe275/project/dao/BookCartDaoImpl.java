package edu.sjsu.cmpe275.project.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.BookCart;

/**
 * @author Onkar Ganjewar
 */

@Repository("bookCartDao")
@Transactional
public class BookCartDaoImpl extends AbstractDao<Serializable, BookCart> implements BookCartDao {

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insert(BookCart entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void remove(BookCart entity) {
		delete(entity);
	}

	@Override
	public List<BookCart> findByUserId(int userId) {
		logger.info("User Id : {}", userId);
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("userId", userId));
		List<BookCart> bookCartList = (List<BookCart>) crit.list();
		if(bookCartList!=null && bookCartList.size()>0){
			for (BookCart cart : bookCartList) {
				logger.info(cart.toString());
			}
		}
		return bookCartList;
	}
}
