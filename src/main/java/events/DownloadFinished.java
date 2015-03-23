package events;

import com.github.nikit.cpp.player.Song;

public class DownloadFinished {
	
	private Song song;

	public DownloadFinished(Song s) {
		this.song = s;
	}

	public Song getSong() {
		return song;
	}
}
