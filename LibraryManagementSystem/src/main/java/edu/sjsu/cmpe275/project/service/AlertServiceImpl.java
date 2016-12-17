package edu.sjsu.cmpe275.project.service;

import org.slf4j.Logger; 
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.sjsu.cmpe275.project.model.Checkout;

/**
 * @author Onkar Ganjewar
 */
@Service("alertService")
@Transactional
public class AlertServiceImpl implements AlertService {

	static final Logger LOGGER = LoggerFactory.getLogger(AlertServiceImpl.class);

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void sendAlerts(Date date, boolean debug) {

		// Check for all the books that are due within 5 days
		List<Map<Checkout, Integer>> checkoutCopiesDue = checkDueDates(date);

		if (checkoutCopiesDue.isEmpty())
			throw new IllegalArgumentException("No copies due");

		if (debug) {
			for (Map<Checkout, Integer> map : checkoutCopiesDue) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue()); 
					Checkout checkout = (Checkout) pair.getKey();
					Integer dueWithin = (Integer) pair.getValue();
//					notificationService.sendRegistrationConfirmationMail(checkout.getUser(),"ASDASD");
					notificationService.sendDueDateAlertMail(checkout, dueWithin);
					it.remove(); // avoids a ConcurrentModificationException
				}	
			}
		} else {
			for (Map<Checkout, Integer> map : checkoutCopiesDue) {
				Iterator it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry) it.next();
					System.out.println(pair.getKey() + " = " + pair.getValue()); 
					Checkout checkout = (Checkout) pair.getKey();
					Integer dueWithin = (Integer) pair.getValue();
					LOGGER.debug(checkout + " = " + dueWithin);
					it.remove(); // avoids a ConcurrentModificationException
				}	
			}
		}			
	}
//				notificationService.sendDueDateAlertMail(checkout, dueWithin);

	/**
	 * 
	 * @param date
	 * @return
	 * @throws IllegalArgumentException
	 */
	private List <Map<Checkout, Integer>> checkDueDates(Date date) {
		List <Map<Checkout, Integer>> datesArr = new ArrayList<Map<Checkout, Integer>>();
		List<Checkout> allCheckedOut = checkoutService.findAllRecords();
		for (Checkout checkout : allCheckedOut) {
			Map<Checkout, Integer> checkoutDateDiffMap = new HashMap<Checkout, Integer>();
			Date dateCheckedOut = checkout.getCheckoutDate();
			Date dueDate = DateUtils.addMonths(dateCheckedOut, 1);
			if (date.after(dueDate)) {
				continue;
				// throw new IllegalArgumentException("Date should be set before
				// due date");
			} else if ( getDateDiffInDays(DateUtils.addMonths(dateCheckedOut, 1), date) <= 5) {
				int daysDiff = getDateDiffInDays(DateUtils.addMonths(dateCheckedOut, 1), date);
				checkoutDateDiffMap.put(checkout, getDateDiffInDays(DateUtils.addMonths(dateCheckedOut, 1), date));
				datesArr.add(checkoutDateDiffMap);
			}
		}
		return datesArr;
	}

	/**
	 * @param checkoutDate
	 * @param date
	 */
	private int getDateDiffInDays(Date checkoutDate, Date date) {
		return (int) ((checkoutDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * Get a diff between two dates
	 * 
	 * @param date1
	 *            the oldest date
	 * @param date2
	 *            the newest date
	 * @param timeUnit
	 *            the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	// public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit)
	// {
	// long diffInMillies = date2.getTime() - date1.getTime();
	// return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	//
	// Date startDate = date1; // Set start date
	// Date endDate = date2; // Set end date
	//
	// long duration = endDate.getTime() - startDate.getTime();
	//
	// long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
	// long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
	// long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
	//
	// }

}
