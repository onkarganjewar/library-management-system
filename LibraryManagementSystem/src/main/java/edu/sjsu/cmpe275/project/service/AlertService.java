package edu.sjsu.cmpe275.project.service;

import java.util.Date;

/**
 * @author Onkar Ganjewar
 */
public interface AlertService {
	/**
	 * @param date
	 * @param debug
	 * @throws IllegalArgumentException
	 */
	public void sendAlerts(Date testDate, boolean debug);
	public void generateFines(Date testDate, boolean debug);
}
