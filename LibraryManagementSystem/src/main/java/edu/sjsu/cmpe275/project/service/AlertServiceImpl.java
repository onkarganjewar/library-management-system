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
	private UserService userService;

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private NotificationService notificationService;

	@Override
	public void sendAlerts(Date testDate, boolean debug) {

		// Check for all the books that are due within 5 days
		List<Map<Checkout, Integer>> checkoutCopiesDue = checkDueDates(testDate);

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

	@Override
	public void generateFines(Date testDate, boolean debug) {
		
		List<Checkout> allCheckedOut = checkoutService.findAllRecords();
		for (Checkout checkout : allCheckedOut) {
			int userId = (int)checkout.getUserId();
			Date dueDate = calculateDueDate(checkout.getCheckoutDate());
			if (dueDate.before(testDate)) {
				int daysOverDue = getDateDiffInDays(testDate, dueDate);
				userService.generateFines(userId, daysOverDue);
			}
		}
	}

	
	/**
	 * Returns the list of checked out books that are due within 5 days
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
	 * @return due date
	 */
	private Date calculateDueDate(Date checkoutDate) {
		return DateUtils.addMonths(checkoutDate, 1);
	}

	/**
	 * 
	 * @param newerDate
	 * @param olderDate
	 * @return
	 */
	private int getDateDiffInDays(Date newerDate, Date olderDate) {
		long diffInMili = (newerDate.getTime() - olderDate.getTime()) ;
		long hrsConverter = (1000 * 60 * 60 * 24);
		long division = diffInMili / hrsConverter;
		float remainder = diffInMili % hrsConverter;
	
		if ((division > 0) && (remainder > 0))
			return (int) (division + 1);
		return (int) division;
//		return (int) ((newerDate.getTime() - olderDate.getTime()) / (1000 * 60 * 60 * 24));
	}

}
