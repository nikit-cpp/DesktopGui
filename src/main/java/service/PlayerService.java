package service;


import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Invoke;
import net.engio.mbassy.listener.Listener;

import org.apache.log4j.Logger;

import events.DownloadEvent;

@Listener
public class PlayerService {
	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

    @Handler//(delivery = Invoke.Asynchronously)
	  public void recordCustomerChange(DownloadEvent e) {
		  LOGGER.debug("Message from PlayerService");
	  }

}
