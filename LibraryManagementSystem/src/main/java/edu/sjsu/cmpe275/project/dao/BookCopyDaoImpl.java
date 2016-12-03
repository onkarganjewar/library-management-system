/**
 * 
 */
package edu.sjsu.cmpe275.project.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.BookCopy;

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

}
