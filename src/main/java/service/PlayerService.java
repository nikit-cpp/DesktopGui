package service;

import java.io.File;

import org.apache.log4j.Logger;

import events.PlayFinished;
import player.Player;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;
import events.DownloadFinished;
import events.NextSong;
import events.PlayDemandEvent;
import events.PlayEvent;

public class PlayerService {

	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	private EventBus eventBus;
	private Player player;
	private PlayList playList;
	private Song currentSong;

	private void play(Song song) {
		try {
			currentSong = song;
			player.prepareFor(song.getFile().getAbsolutePath());
			player.play();
		} catch (Exception e) {
			LOGGER.error("Error!!!", e);
		}
	}
	
	boolean mayNext = true;
	
	@Subscribe
	public void playDemand(PlayDemandEvent e) {
		LOGGER.debug("playDemand()");
		Song song = e.getSong();
		File dest = song.getFile();
		mayNext = false;
		LOGGER.debug("setting mayNext=" + mayNext);

		if (dest == null) {
			eventBus.post(new DownloadEvent(song));
		} else {
			play(song);
		}
	}

	@Subscribe
	public void play(PlayEvent e) {
		LOGGER.debug("play()");
		Song song = e.getSong();
		File dest = song.getFile();
		if (dest == null) {
			eventBus.post(new DownloadEvent(song));
		} else {
			play(song);
		}
	}
	
	@Subscribe
	public void playAfterDownloadFinished(DownloadFinished e) {
		Song song = e.getSong();
		eventBus.post(new PlayEvent(song));
	}
	
	@Subscribe
	public void onPlayFinished(PlayFinished e) {
		LOGGER.debug("onPlayFinished() mayNext="+mayNext);
		if(mayNext){
			eventBus.post(new NextSong());
		}else{
			mayNext = true;
			LOGGER.debug("setting mayNext="+mayNext);
		}
	}

	@Subscribe
	public void next(NextSong e) {
		Song nextSong = playList.getNextSong(currentSong);
		if (nextSong != null) {
			LOGGER.debug("switching to next");
			eventBus.post(new PlayEvent(nextSong));
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayList getPlayList() {
		return playList;
	}

	public void setPlayList(PlayList playList) {
		this.playList = playList;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
