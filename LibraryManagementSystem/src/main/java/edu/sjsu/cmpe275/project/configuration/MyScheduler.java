package edu.sjsu.cmpe275.project.configuration;

import org.hibernate.service.spi.ServiceException; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import edu.sjsu.cmpe275.project.service.AlertService;
import edu.sjsu.cmpe275.project.util.CustomTimeService;

/**
 * @author Onkar Ganjewar
 */

@EnableScheduling
@Component
public class MyScheduler {

	static final Logger logger = LoggerFactory.getLogger(MyScheduler.class);

	@Autowired
	private AlertService alertService;

	@Autowired
	private CustomTimeService myTimeService;

	/**
	 * Runs a cron scheduler for everyday at 3 am everyday to check if there are any
	 * alerts or fines to be generated/sent.
	 */
	@Scheduled(cron = "0 0 3 * * ?")
	public void scheduleRun() {
		// complete scheduled work
		logger.info("INFO LEVEL");
		logger.debug("Started scheduling");
		logger.info("STARTED DEUGG");
		try {
			alertService.sendAlerts(myTimeService.getDate(), true);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error("Something went wrong");
		}

		try {
			alertService.generateFines(myTimeService.getDate(), true);
		} catch (ServiceException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.error("Something went wrong");
		}

	}
}
