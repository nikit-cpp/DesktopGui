package service;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import events.PlayStopped;
import player.Player;
import player.State;

import com.github.nikit.cpp.player.PlayList;
import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import events.DownloadEvent;
import events.DownloadFinished;
import events.NextSong;
import events.PauseEvent;
import events.PlayEvent;
import events.PlayedProgress;

public class PlayerService {

	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	private EventBus eventBus;
	private Player player;
	private PlayList playList;
	private Song currentSong;
	private int songMaxSize;
	private boolean songmaxSizeSetted = false;
	private volatile AtomicBoolean isPaused = new AtomicBoolean(false);


	private void play(Song song) {
		try {
			currentSong = song;
			player.stop();
			player.play(currentSong);
			isPaused.set(false);
		} catch (Exception e) {
			LOGGER.error("Error!!!", e);
		}
	}
	
	boolean mayNextOnFinished = true;
	
	@AllowConcurrentEvents
	@Subscribe
	public void play(PlayEvent e) {
		LOGGER.debug("play()");
		if(player.getState()==State.PLAYING){
			mayNextOnFinished = false;
		}
		LOGGER.debug("play setted mayNext=" + mayNextOnFinished);
		
		Song song = e.getSong();
		File dest = song.getFile();
		if (dest == null) {
			eventBus.post(new DownloadEvent(song));
		} else {
			play(song);
		}
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayed(final PlayedProgress playedProgress){
		if(!songmaxSizeSetted){
			songmaxSizeSetted = true;
			songMaxSize = playedProgress.getAvailable();
		}
	}

	
	@AllowConcurrentEvents
	@Subscribe
	public void playAfterDownloadFinished(DownloadFinished e) {
		Song song = e.getSong();
		eventBus.post(new PlayEvent(song));
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayFinished(PlayStopped e) {
		songmaxSizeSetted = false;
		songMaxSize = 0;
		LOGGER.debug("onPlayFinished() mayNext="+mayNextOnFinished);
		if(mayNextOnFinished){
			eventBus.post(new NextSong());
		}else{
			mayNextOnFinished = true;
			LOGGER.debug("setting mayNext="+mayNextOnFinished);
		}
	}

	@AllowConcurrentEvents
	@Subscribe
	public void next(NextSong e) {
		Song nextSong = playList.getNextSong(currentSong);
		if (nextSong != null) {
			LOGGER.debug("switching to next");
			eventBus.post(new PlayEvent(nextSong));
		}
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPause(PauseEvent e){
		mayNextOnFinished = false;
		isPaused.set(true);
		player.pause();
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

	public int getSongMaxSize() {
		return songMaxSize;
	}

	public AtomicBoolean getPaused() {
		return isPaused;
	}
}
