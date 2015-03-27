package events;

import com.github.nikit.cpp.player.Song;

public class PlayDemandEvent {
	private Song song;
	public PlayDemandEvent(Song song) {
		this.song = song;
	}
	public Song getSong() {
		return song;
	}
}
