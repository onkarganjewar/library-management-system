package edu.sjsu.cmpe275.project.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.HoldListDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.BookCopy;
import edu.sjsu.cmpe275.project.model.BooksHoldList;
import edu.sjsu.cmpe275.project.model.User;

/**
 * @author Onkar Ganjewar
 */
@Service("holdListService")
@Transactional
public class HoldListServiceImpl implements HoldListService {

	@Autowired
	private HoldListDao holdListDao;
	
	@Override
	public void addRecord(BooksHoldList entity) {
		holdListDao.insert(entity);
	}

	@Override
	public void deleteRecordById(Integer holdListId) {
		holdListDao.removeByHoldListId(holdListId);
	}

	@Override
	public List<User> findAllUsersForBook(Integer bookId) {
		List<User> usersList = new ArrayList<User>();
		List<BooksHoldList> holdListArr = holdListDao.findByBookId(bookId);
		for (BooksHoldList holdList : holdListArr) {
			usersList.add(holdList.getUser());
		}
		return usersList;
	}

	@Override
	public List<Book> findAllBooksForUser(Integer userId) {
		List<Book> booksList = new ArrayList<Book>();
		List<BooksHoldList> holdListArr = holdListDao.findByUserId(userId);
		for (BooksHoldList holdList : holdListArr) {
			booksList.add(holdList.getBook());
		}
		return booksList;
	}

	@Override
	public BooksHoldList getFirstInLineForBook(Integer bookId) {
		List<BooksHoldList> holdListArr = holdListDao.findByBookId(bookId);
		if (holdListArr.isEmpty() || holdListArr == null)
			throw new ServiceException("No hold list for this book");
		return holdListArr.get(0);
	}

	@Override
	public void removeUserFromHoldList(Integer userId, Integer bookId) {
		List<BooksHoldList> holdListArr = holdListDao.findByBookId(bookId);
		for (BooksHoldList holdList : holdListArr) {
			if (holdList.getUserId().equals(userId)) {
				holdListDao.removeByHoldListId(holdList.getHoldListId());
				break;
			}
		}
	}

	@Override
	public BooksHoldList getRecordByHoldListId(Integer holdListId) {
		return holdListDao.findByHoldListId(holdListId);
	}

	@Override
	public List<BooksHoldList> findAllRecords() {
		return holdListDao.findAllRecords();
	}

	@Override
	public void updateRecord(BooksHoldList record) {
		holdListDao.modify(record);
	}

	@Override
	public List<BookCopy> findAllBookCopies(Integer bookId) {
		List<BooksHoldList> holdList =  holdListDao.findByBookId(bookId);
		List<BookCopy> copiesList = new ArrayList<BookCopy>();
		for (BooksHoldList booksHoldList : holdList) {
			copiesList.add(booksHoldList.getBookCopy());
		}
		return copiesList;
	}
	@Override
	public List<BooksHoldList> findAllRecordsByBookId(Integer bookId) {
		return holdListDao.findByBookId(bookId);
	}
}
