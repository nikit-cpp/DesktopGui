package service;

import java.awt.EventQueue;
import java.io.File;
import java.util.UUID;

import org.apache.log4j.Logger;

import player.PlayFinished;
import player.Player;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;
import events.DownloadFinished;
import events.NextSong;
import events.OnDemandPlayEvent;
import events.AutomaticPlayEvent;

public class PlayerService {
	
	@SuppressWarnings("unused")
	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	private EventBus eventBus;
	private Player player;
	private PlayList playList;
	private Song currentSong;

	private boolean stoppedByDemand = false;;
		
	private void play(Song song) {
		currentSong = song;
		player.prepareFor(song.getFile().getAbsolutePath());
		player.play();
	}

	@Subscribe
	synchronized public void automaticPlay(AutomaticPlayEvent e){
		Song song = e.getSong();
		File dest = song.getFile();
		if(dest == null){
			eventBus.post(new DownloadEvent(song));
		} else {
			play(song);
		}
	}
	
	@Subscribe
	public void playAfterDownloadFinished(DownloadFinished e) {
		Song song = e.getSong();
		eventBus.post(new AutomaticPlayEvent(song));
	}
	
	@Subscribe
	synchronized public void onDemandPlay(OnDemandPlayEvent e){
		Song song = e.getSong();
		stoppedByDemand = true;
		eventBus.post(new AutomaticPlayEvent(song));
	}
	
	@Subscribe
	public void onPlayFinished(PlayFinished e){
		LOGGER.debug("Switching to next Song...");
		if(!stoppedByDemand ){
			stoppedByDemand = false;
			eventBus.post(new NextSong());
		}
	}
	
	@Subscribe
	public void next(NextSong e){
		Song nextSong = playList.getNextSong(currentSong);
		if(nextSong!=null){
			eventBus.post(new AutomaticPlayEvent(nextSong));
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
