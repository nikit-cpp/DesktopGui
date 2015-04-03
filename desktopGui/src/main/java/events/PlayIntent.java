package events;

import com.github.nikit.cpp.player.Song;

public class PlayIntent {
	private Song song;
	public PlayIntent(Song song) {
		this.song = song;
	}
	public Song getSong() {
		return song;
	}

}
