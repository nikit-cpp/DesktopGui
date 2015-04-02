package player;

import com.github.nikit.cpp.player.Song;

public interface Player {
	void play(Song song);
	void pause();
	void stop();
	State getState();
}
