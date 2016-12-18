package edu.sjsu.cmpe275.project.service;

import java.util.Date;

import org.hibernate.service.spi.ServiceException;

/**
 * @author Onkar Ganjewar
 */
public interface AlertService {
	/**
	 * Sends email alerts to the patron whose books are due within 5 days
	 * @param testDate
	 * @param debug
	 * @throws ServiceException if there are no checked out copies that are past due
	 */
	public void sendAlerts(Date testDate, boolean debug);
	/**
	 * Calculates fine for all the books that are overdue 
	 * @param testDate
	 * @param debug
	 * @throws ServiceException if there are no checked out copies that are past due
	 */
	public void generateFines(Date testDate, boolean debug);
}
