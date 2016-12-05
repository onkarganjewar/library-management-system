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
	public boolean saveBook(Book book) {
		List<Book> existingBooks = bookDao.findByTitle(book.getTitle());
		if (existingBooks != null && existingBooks.size() > 0 ) {
			for (Book b : existingBooks) {
				if (b.getTitle().equals(book.getTitle()) && b.getPublisher().equals(book.getPublisher()) && b.getAuthor().equals(book.getAuthor())) {
					return false;
				}
			}
		}
		bookDao.save(book);
		return true;
	}

	@Override
	public void updateBook(Book book) {
		sessionFactory.getCurrentSession().update(book);
//		Book entity = bookDao.findbyId(book.getId().toString());
//		if (entity != null) {
//			entity.setAuthor(book.getAuthor());
//			entity.setAvailability(book.getAvailability());
//			entity.setCallNumber(book.getCallNumber());
//			entity.setCopies(book.getCopies());
//			entity.setKeywords(book.getKeywords());
//			entity.setLibraryLocation(book.getLibraryLocation());
//			entity.setPublicationYear(book.getPublicationYear());
//			entity.setPublisher(book.getPublisher());
//			entity.setTitle(book.getTitle());
//			entity.setCheckoutCopies(book.getCheckoutCopies());
//		}
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
