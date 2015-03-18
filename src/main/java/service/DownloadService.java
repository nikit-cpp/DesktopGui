package service;


import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;

public class DownloadService {
	private static Logger LOGGER = Logger.getLogger(DownloadService.class);

	  @Subscribe public void recordCustomerChange(DownloadEvent e) {
		  LOGGER.debug("Message from PlayerService");
	  }

}
