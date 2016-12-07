package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.MyCalendar;

/**
 * @author Onkar Ganjewar
 */
public interface MyCalendarDao {
	
	public void insert(MyCalendar cal);
	public List<MyCalendar> findByCurrentTime (MyCalendar cal);
	public void removeAll();
}
