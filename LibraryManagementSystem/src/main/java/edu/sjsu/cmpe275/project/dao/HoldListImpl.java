package edu.sjsu.cmpe275.project.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.BooksHoldList;
import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */

@Repository("holdListDao")
@Transactional
public class HoldListImpl extends AbstractDao<Serializable, BooksHoldList> implements HoldListDao {

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public BooksHoldList findByHoldListId(Integer holdListId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("holdListId", holdListId));
		BooksHoldList bookHoldList = (BooksHoldList) crit.uniqueResult();
		return bookHoldList;
	}

	@Override
	public void insert(BooksHoldList entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void remove(BooksHoldList entity) {
		delete(entity);
	}

	@Override
	public void modify(BooksHoldList entity) {
		update(entity);
	}

	@Override
	public void removeByHoldListId(Integer holdListId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("holdListId", holdListId));
		BooksHoldList holdList = (BooksHoldList) crit.uniqueResult();
		delete(holdList);
	}

	@Override
	public List<BooksHoldList> findByBookId(Integer bookId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("bookId", bookId));
		crit.addOrder(Order.asc("dateAssigned"));
		List<BooksHoldList> holdListArr = (List<BooksHoldList>) crit.list();
		for (BooksHoldList holdList : holdListArr) {
			Hibernate.initialize(holdList.getUser());
			Hibernate.initialize(holdList.getBook());
			Hibernate.initialize(holdList.getBookCopy());
		}
		return holdListArr;
	}

	@Override
	public List<BooksHoldList> findByUserId(Integer userId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("userId", userId));
		crit.addOrder(Order.asc("dateAssigned"));
		List<BooksHoldList> holdListArr = (List<BooksHoldList>) crit.list();
		for (BooksHoldList holdList : holdListArr) {
			Hibernate.initialize(holdList.getUser());
			Hibernate.initialize(holdList.getBook());
			Hibernate.initialize(holdList.getBookCopy());
		}
		return holdListArr;
	}

	@Override
	public List<BooksHoldList> findAllRecords() {
		return sessionFactory.getCurrentSession().createCriteria(WaitList.class).addOrder(Order.asc("dateAssigned")).list();
	}

	@Override
	public BooksHoldList findByBookCopyId(Integer copyId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("bookCopyId", copyId));
		BooksHoldList holdList = (BooksHoldList) crit.uniqueResult();
		return holdList;
	}

}
