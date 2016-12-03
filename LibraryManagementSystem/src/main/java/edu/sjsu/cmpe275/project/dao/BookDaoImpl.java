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
 */

/**
 * 
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
	public Book findByTitle(String title) {
		Criteria crit = createEntityCriteria();
		crit.add(Restrictions.eq("title", title));
		List<Book> listBook = new ArrayList<Book>();
		
		listBook = (List<Book>) crit.list();
		Book book1 = new Book();
		if (listBook!=null) {
			book1 = listBook.get(0);
			for (Book book : listBook) {
				System.out.println(book);
			}
		}
//		if (user != null) {
//			Hibernate.initialize(user.getCopies());
//		}
		return book1;
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

}
