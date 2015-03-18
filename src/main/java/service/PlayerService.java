package service;


import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;

public class PlayerService {
	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	  @Subscribe public void recordCustomerChange(DownloadEvent e) {
		  LOGGER.debug("Message from PlayerService");
	  }

}
