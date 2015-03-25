package events;

import com.github.nikit.cpp.player.Song;

public class OnDemandPlayEvent {
	private Song song;
	public OnDemandPlayEvent(Song song) {
		this.song = song;
	}
	public Song getSong() {
		return song;
	}
}
