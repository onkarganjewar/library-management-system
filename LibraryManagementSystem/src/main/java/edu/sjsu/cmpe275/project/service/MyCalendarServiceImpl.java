package edu.sjsu.cmpe275.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.dao.MyCalendarDao;
import edu.sjsu.cmpe275.project.model.MyCalendar;

/**
 * @author Onkar Ganjewar
 */

@Service("myCalendarService")
@Transactional
public class MyCalendarServiceImpl implements MyCalendarService{
	
	@Autowired
	private MyCalendarDao myCalendarDao;
	
	@Override
	public void addCalendar (MyCalendar myCal) {
		myCalendarDao.insert(myCal);
	}

	@Override
	public List<MyCalendar> findByDate(MyCalendar myCal) {
		return myCalendarDao.findByCurrentTime(myCal);
	}

	@Override
	public void deleteAllRecords() {
		myCalendarDao.removeAll();
	}
}
