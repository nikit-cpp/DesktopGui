package events;

import com.github.nikit.cpp.player.Song;

public class ProgressEvent {
	private int available;
	private Song song;
	public ProgressEvent(int available, Song song) {
		this.song = song;
		this.available = available;
	}
	public int getAvailable() {
		return available;
	}
	public Song getSong() {
		return song;
	}

}
