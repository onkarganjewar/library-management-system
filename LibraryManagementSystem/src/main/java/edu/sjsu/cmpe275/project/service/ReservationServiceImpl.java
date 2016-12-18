package edu.sjsu.cmpe275.project.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.WaitList;

/**
 * @author Onkar Ganjewar
 */
@Service("reservationService")
@Transactional
public class ReservationServiceImpl implements ReservationService {

	static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

	@Autowired
	private WaitListService waitListService;
	
	@Autowired
	private NotificationService notificationService;

	@Override
	public void checkReservations(Date date, boolean debug) {

		// Query the waitlist table to get all the wait list dates for all the users
		List<Map<WaitList, Integer>> waitListDueList = getWaitingDates(date);
		
		if (waitListDueList.isEmpty())
			throw new IllegalArgumentException("No waitlist records found");
		
		if (debug) {
			for (Map<WaitList, Integer> map : waitListDueList) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue()); 
					WaitList waitList = (WaitList) pair.getKey();
					Integer dueWithin = (Integer) pair.getValue();
//					notificationService.sendWaitListAlertMail(waitList, dueWithin);
					it.remove(); // avoids a ConcurrentModificationException
				}
			}
		} else {
			for (Map<WaitList, Integer> map : waitListDueList) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue()); 
					WaitList waitList = (WaitList) pair.getKey();
					Integer dueWithin = (Integer) pair.getValue();
					LOGGER.debug(waitList + " = " + dueWithin);
					it.remove(); // avoids a ConcurrentModificationException
				}
			}
		}
	}


	/**
	 * Check the given date against the waitlist assigned dates of a user
	 * @param date Custom date to check the database against
	 * @return Array containing the waitlist entity with the number of days remaining to issue the corres book
	 */
	private List<Map<WaitList, Integer>> getWaitingDates(Date date) {
		List<Map<WaitList, Integer>> waitListAlertsArray = new ArrayList<Map<WaitList, Integer>> ();
		List<WaitList> allWaitLists = waitListService.findAllRecords();

		if (allWaitLists.isEmpty())
			throw new IllegalArgumentException("No waitlist records found");
		
		// Check the assigned date for a particular user in the waiting list
		for (WaitList waitList : allWaitLists) {
			Map<WaitList, Integer> waitListAlertMap = new HashMap<WaitList, Integer>();
//			Date dateAssigned = waitList.getDateAssigned();
			Date dateAssigned = waitList.getDateAdded();
			int daysDiff = getDateDiffInDays(date, dateAssigned);
			if (date.before(dateAssigned)) {
				continue;
				// throw new IllegalArgumentException("Date should be set before
				// due date");
			} else if ( daysDiff < 3) {
//				int daysDiff = getDateDiffInDays(date, dateAssigned);
				waitListAlertMap.put(waitList, daysDiff);
				waitListAlertsArray.add(waitListAlertMap);
			}
		}
		return waitListAlertsArray;
	}


	/**
	 * 
	 * @param newerDate
	 * @param olderDate
	 * @return
	 */
	private int getDateDiffInDays(Date newerDate, Date olderDate) {
		return (int) ((newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24));
	}

}
