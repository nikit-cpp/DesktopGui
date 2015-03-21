package service;

import player.Player;

import com.google.common.eventbus.Subscribe;

import events.PlayEvent;

public class PlayerService {
	private Player player;

	@Subscribe
	synchronized public void play(PlayEvent e){
		player.prepareFor(e.getPath());
		player.play();
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
