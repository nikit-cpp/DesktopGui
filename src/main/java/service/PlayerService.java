package service;

import org.apache.log4j.Logger;

import player.PlayFinished;
import player.Player;

import com.google.common.eventbus.Subscribe;

import events.PlayEvent;

public class PlayerService {
	
	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	
	private Player player;

	@Subscribe
	synchronized public void play(PlayEvent e){
		player.prepareFor(e.getPath());
		player.play();
	}
	
	@Subscribe
	synchronized public void play(PlayFinished e){
		LOGGER.debug("Play finished");
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
