package edu.sjsu.cmpe275.project.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.MyCalendar;

/**
 * @author Onkar Ganjewar
 */

@Repository("myCalendarDao")
@Transactional
public class MyCalendarDaoImpl extends AbstractDao<Serializable, MyCalendar> implements MyCalendarDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void insert(MyCalendar cal) {
		persist(cal);
	}

	@Override
	public List<MyCalendar> findByCurrentTime(MyCalendar cal) {
		int day = cal.getDay();
		int month = cal.getMonth();
		int year = cal.getYear();
		Criteria c = createEntityCriteria();
		c.add(Restrictions.eq("year", year));
		c.add(Restrictions.eq("day", day));
		c.add(Restrictions.eq("month", month));
		List<MyCalendar> myCals = (List<MyCalendar>)c.list();
		for (MyCalendar myCalendar : myCals) {
			System.out.println(myCalendar);
		}
		return myCals;
	}

	@Override
	public void removeAll() {
		sessionFactory.getCurrentSession().createQuery("delete from MyCalendar").executeUpdate();		
	}

}
