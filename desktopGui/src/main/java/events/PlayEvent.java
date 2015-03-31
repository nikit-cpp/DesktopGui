package events;

import com.github.nikit.cpp.player.Song;

public class PlayEvent {
	private Song song;
	public PlayEvent(Song song) {
		this.song = song;
	}
	public Song getSong() {
		return song;
	}

}
