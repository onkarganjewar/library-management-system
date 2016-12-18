package edu.sjsu.cmpe275.project.dao;

import java.util.List;

import edu.sjsu.cmpe275.project.model.WaitList;


/**
 * @author Onkar Ganjewar
 */
public interface WaitListDao {

	public WaitList findByWaitListId (Integer waitListId);
	public void insert(WaitList entity);
	public void remove(WaitList entity);
	public void modify(WaitList entity);
	public void removeByWaitListId (Integer waitListId);
	public List<WaitList> findByBookId (Integer bookId);
	public List<WaitList> findByUserId (Integer userId);
	public List<WaitList> findAllRecords ();

}
