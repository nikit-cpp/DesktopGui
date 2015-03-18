package events;

import org.apache.log4j.Logger;

import com.github.nikit.cpp.player.Song;

public class DownloadEvent {
	private static Logger LOGGER = Logger.getLogger(DownloadEvent.class);
	
	private Song song;
	
	public DownloadEvent(Song s){
		LOGGER.debug("DownloadEvent instantiated for " + s);
		this.song = s;
	}

	public Song getSong() {
		return song;
	}
}
