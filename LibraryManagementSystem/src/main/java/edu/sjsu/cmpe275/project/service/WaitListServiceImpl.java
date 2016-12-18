package edu.sjsu.cmpe275.project.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.WaitListDao;
import edu.sjsu.cmpe275.project.model.Book;
import edu.sjsu.cmpe275.project.model.User;
import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */
@Service("waitListService")
@Transactional
public class WaitListServiceImpl implements WaitListService {

	@Autowired
	private WaitListDao waitListDao;

	@Override
	public void addRecord(WaitList entity) {
		waitListDao.insert(entity);
	}

	@Override
	public void deleteRecordById(Integer id) {
		waitListDao.removeByWaitListId(id);
	}

	@Override
	public List<User> findAllUsersForBook(Integer bookId) {
		List<User> usersList = new ArrayList<User>();
		List<WaitList> waitListArr = waitListDao.findByBookId(bookId);
		for (WaitList waitList : waitListArr) {
			usersList.add(waitList.getUser());
		}
		return usersList;
	}

	@Override
	public List<Book> findAllBooksForUser(Integer userId) {
		List<Book> booksList = new ArrayList<Book>();
		List<WaitList> waitListArr = waitListDao.findByUserId(userId);
		for (WaitList waitList : waitListArr) {
			booksList.add(waitList.getBook());
		}
		return booksList;
	}

	@Override
	public WaitList getFirstInLineForBook(Integer bookId) {
		List<WaitList> waitListArr = waitListDao.findByBookId(bookId);
		if (waitListArr == null || waitListArr.isEmpty())
			throw new ServiceException("No waiting list");
		return waitListArr.get(0);
	}

	@Override
	public void removeUserFromWaitList(Integer userId, Integer bookId) {
		List<WaitList> waitListArr = waitListDao.findByBookId(bookId);
		for (WaitList waitList : waitListArr) {
			if (waitList.getUserId().equals(userId)) {
				waitListDao.removeByWaitListId(waitList.getWaitListId());
				break;
			}
		}
	}

	@Override
	public WaitList getRecordByWaitListId(Integer waitListId) {
		return waitListDao.findByWaitListId(waitListId);
	}

	@Override
	public List<WaitList> findAllRecords() {
		return waitListDao.findAllRecords();
	}

	@Override
	public void updateRecord(WaitList record) {
		// TODO Auto-generated method stub
		waitListDao.modify(record);
	}
}
