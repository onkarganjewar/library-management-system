package edu.sjsu.cmpe275.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.BookCopyDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;

/**
 * @author Onkar Ganjewar
 */
@Service("bookCopyService")
@Transactional
public class BookCopyServiceImpl implements BookCopyService{

	@Autowired
	BookCopyDao bookCopyDao;
	
	@Override
	public void saveCopy(BookCopy copy) {
		bookCopyDao.save(copy);
	}

	@Override
	public List<BookCopy> findAllByBook(Book book) {
		return bookCopyDao.findByBook(book);
	}

	@Override
	public void deleteCopy(BookCopy copy) {
		bookCopyDao.remove(copy);
	}

}
