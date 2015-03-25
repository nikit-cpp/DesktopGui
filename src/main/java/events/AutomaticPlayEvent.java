package events;

import com.github.nikit.cpp.player.Song;

public class AutomaticPlayEvent {
	private Song song;
	public AutomaticPlayEvent(Song song) {
		this.song = song;
	}
	public Song getSong() {
		return song;
	}
}
