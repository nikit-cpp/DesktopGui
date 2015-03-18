package events;

import org.apache.log4j.Logger;

public class DownloadEvent {
	private static Logger LOGGER = Logger.getLogger(DownloadEvent.class);
	
	public DownloadEvent(){
		LOGGER.debug("DownloadEvent instantiated");
	}
}
