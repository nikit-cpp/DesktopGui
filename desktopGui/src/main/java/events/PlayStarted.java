package events;

import com.github.nikit.cpp.player.Song;

public class PlayStarted {

	public PlayStarted(Song playedSong) {
		this.song = playedSong;
	}
	
	private Song song;

	public Song getSong() {
		return song;
	}


	
}
