package events;

import com.github.nikit.cpp.player.Song;

public class PlayStopped {
	public PlayStopped(Song song) {
		super();
		this.song = song;
	}

	private Song song;

	public Song getSong() {
		return song;
	}
	
}
