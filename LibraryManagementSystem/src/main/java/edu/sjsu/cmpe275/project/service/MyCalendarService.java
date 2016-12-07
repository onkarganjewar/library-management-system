package edu.sjsu.cmpe275.project.service;

import java.util.List;

import edu.sjsu.cmpe275.project.model.MyCalendar;

/**
 * @author Onkar Ganjewar
 */
public interface MyCalendarService {
	
	void addCalendar (MyCalendar myCal);
	List<MyCalendar> findByDate (MyCalendar myCal);
	void deleteAllRecords();
	
}
