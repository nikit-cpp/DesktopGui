package service;


import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;

import org.apache.log4j.Logger;

import events.DownloadEvent;

@Listener
public class DownloadListener {
	private static Logger LOGGER = Logger.getLogger(DownloadListener.class);

    @Handler//(delivery = Invoke.Asynchronously)
	  public void handleDownloadEvent(DownloadEvent e) {
		  LOGGER.debug("Message from PlayerService");
	  }

}
