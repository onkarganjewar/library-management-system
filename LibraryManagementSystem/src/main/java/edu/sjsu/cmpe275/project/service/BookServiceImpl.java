package edu.sjsu.cmpe275.project.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.BookDao;
import edu.sjsu.cmpe275.project.model.Book;

/**
 * @author Onkar Ganjewar
 */
@Service("bookService")
@Transactional
public class BookServiceImpl implements BookService {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	BookDao bookDao;

	@Override
	public Book findById(String id) {
		return bookDao.findbyId(id);
	}

	@Override
	public List<Book> findByTitle(String title) {
		return bookDao.findByTitle(title);
	}
	@Override
	public Integer saveBook(Book book) {
		List<Book> existingBooks = bookDao.findByTitle(book.getTitle());
		if (existingBooks != null && !existingBooks.isEmpty()) {
			for (Book b : existingBooks) {
				if (b.getTitle().equals(book.getTitle()) && b.getPublisher().equals(book.getPublisher()) && b.getAuthor().equals(book.getAuthor()) && b.getCallNumber().equals(book.getCallNumber())) {
					return -1;
				}
			}
		}
		bookDao.save(book);
		System.out.println("Book ID = "+book.getId());
		return book.getId();
	}

	@Override
	public void updateBook(Book book) {
		sessionFactory.getCurrentSession().update(book);
	}

	@Override
	public void deleteBook(int id) {
		bookDao.deleteById(id);
	}

	@Override
	public List<Book> findAllBooks() {
		return bookDao.findAllBooks();
	}
}
