package service;

import com.google.common.eventbus.Subscribe;

import events.PlayEvent;
import vkButtonedMp3Player.CustomPlayer;

public class PlayService {
	private CustomPlayer player = new CustomPlayer();

	@Subscribe
	synchronized public void play(PlayEvent e){
		player.setPath(e.getPath());
		player.play();
	}
}
