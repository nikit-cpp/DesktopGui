package events;

import org.apache.log4j.Logger;

public class DownloadEvent {
	private static Logger LOGGER = Logger.getLogger(DownloadEvent.class);
	
	private String url;
	
	public DownloadEvent(String url){
		LOGGER.debug("DownloadEvent instantiated for " + url);
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
