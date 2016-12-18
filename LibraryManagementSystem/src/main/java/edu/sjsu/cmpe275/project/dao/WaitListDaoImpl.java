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

import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */
@Repository("waitListDao")
@Transactional
public class WaitListDaoImpl extends AbstractDao<Serializable, WaitList> implements WaitListDao {

	static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insert(WaitList entity) {
		try {
			sessionFactory.getCurrentSession().save(entity);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void remove(WaitList entity) {
		delete(entity);
	}

	@Override
	public List<WaitList> findByBookId(Integer bookId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("bookId", bookId));
		crit.addOrder(Order.asc("dateAdded"));
		List<WaitList> waitListArr = (List<WaitList>) crit.list();
		for (WaitList waitList : waitListArr) {
			Hibernate.initialize(waitList.getUser());
			Hibernate.initialize(waitList.getBook());
		}
		return waitListArr;
	}

	@Override
	public List<WaitList> findByUserId(Integer userId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("userId", userId));
//		crit.addOrder(Order.asc("waitListId"));
		List<WaitList> waitListArr = (List<WaitList>) crit.list();
		for (WaitList waitList : waitListArr) {
			Hibernate.initialize(waitList.getUser());
			Hibernate.initialize(waitList.getBook());
		}
		return waitListArr;
	}

	@Override
	public WaitList findByWaitListId(Integer waitListId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("waitListId", waitListId));
		WaitList waitList = (WaitList) crit.uniqueResult();
		return waitList;
	}

	@Override
	public void removeByWaitListId(Integer waitListId) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("waitListId", waitListId));
		WaitList waitList = (WaitList) crit.uniqueResult();
		delete(waitList);
	}

	@Override
	public List<WaitList> findAllRecords() {
		return sessionFactory.getCurrentSession().createCriteria(WaitList.class).addOrder(Order.asc("dateAdded")).list();
	}

	@Override
	public void modify(WaitList entity) {
		// TODO Auto-generated method stub
		update(entity);
	}
}
