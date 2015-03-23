package service;

import org.apache.log4j.Logger;

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
		LOGGER.debug("This message must be showed after song playing completed!");
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
