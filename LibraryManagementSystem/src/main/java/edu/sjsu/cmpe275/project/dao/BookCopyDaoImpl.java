package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

/**
 * @author Onkar Ganjewar
 */
@Repository("bookCopyDao")
@Transactional
public class BookCopyDaoImpl extends AbstractDao<Integer, BookCopy> implements BookCopyDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void save(BookCopy bookCopy) {
		merge(bookCopy);
	}

	@Override
	public List<BookCopy> findByBook (Book book) {

		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("books", book));
		List<BookCopy> bcList = (List<BookCopy>) crit.list();
		for (BookCopy bookCopy : bcList) {
			Hibernate.initialize(bookCopy.getBooks());
		}
		return bcList;
	}	
	
	@Override
	public void remove(BookCopy entity) {
		sessionFactory.getCurrentSession().delete(entity);
	}
}
