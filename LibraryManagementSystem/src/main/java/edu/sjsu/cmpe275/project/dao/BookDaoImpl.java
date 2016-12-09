package edu.sjsu.cmpe275.project.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 * @author Shreya Prabhu
 *
 */

@Repository("bookDao")
@Transactional
public class BookDaoImpl extends AbstractDao<Integer, Book> implements BookDao {

	@Override
	public void save(Book book) {
		persist(book);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.sjsu.cmpe275.project.dao.BookDao#findByTitle(java.lang.String)
	 */
	@Override
	public List<Book> findByTitle(String title) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("title", title));
		List<Book> bookList = new ArrayList<Book>();
		
		bookList = (List<Book>) crit.list();
		Book book1 = new Book();
		if (bookList!=null && bookList.size()>0) {
			book1 = bookList.get(0);
		}
		return bookList;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe275.project.dao.BookDao#deleteBook(edu.sjsu.cmpe275.project.model.Book)
	 */
	@Override
	public void deleteById(int id) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", id));
		Book user = (Book)crit.uniqueResult();
		delete(user);		
	}

	@Override
	public List<Book> findAllBooks() {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);//To avoid duplicates.
		List<Book> books = (List<Book>) criteria.list();
		return books;
	}

	@Override
	public Book findbyId(String id) {
		Integer intId = Integer.parseInt(id);
		
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("id", intId));
		Book book = (Book)crit.uniqueResult();
		if(book!=null) {
			Hibernate.initialize(book.getCheckoutCopies());
		}
		return book;
	}

	@Override
	public void modify(Book book) {
		update(book);
	}
}
