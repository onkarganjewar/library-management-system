/**
 * 
 */
package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 * 
 */
@Repository("bookCopyDao")
@Transactional
public class BookCopyDaoImpl extends AbstractDao<Integer, BookCopy> implements BookCopyDao {

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.project.dao.BookCopyDao#save(edu.sjsu.cmpe275.project.model.BookCopy)
	 */
	@Override
	public void save(BookCopy bookCopy) {
		// TODO Auto-generated method stub
//		persist(bookCopy);
		merge(bookCopy);
	}

	@Override
	public List<BookCopy> findByBook (Book book) {

		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("books", book));
		List<BookCopy> bcList = (List<BookCopy>) crit.list();
		for (BookCopy bookCopy : bcList) {
			System.out.println(bookCopy.getId());
		}
		return bcList;
	}	
	
	@Override
	public void delete(BookCopy entity) {
		super.delete(entity);
	}
}
