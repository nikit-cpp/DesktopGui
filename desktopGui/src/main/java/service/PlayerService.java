package service;

import java.io.File;

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
import events.PlayPauseEvent;
import events.PlayIntent;
import events.ProgressEvent;
import events.PrevSong;
import events.StopIntent;

public class PlayerService {

	private static Logger LOGGER = Logger.getLogger(PlayerService.class);

	private EventBus eventBus;
	private Player player;
	private PlayList playList;
	private Song currentSong;
	private int songMaxSize;
	private boolean songmaxSizeSetted = false;
	private boolean mayNextOnFinished = true;
	private volatile boolean isPaused = false;

		
	@AllowConcurrentEvents
	@Subscribe
	public void play(PlayIntent e) {
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
	
	private void play(Song song) {
		try {
			currentSong = song;
			player.stop();
			player.play(currentSong);
			isPaused=false;
		} catch (Exception e) {
			LOGGER.error("Error!!!", e);
		}
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayingProgress(final ProgressEvent playedProgress){
		if(!songmaxSizeSetted){
			songmaxSizeSetted = true;
			songMaxSize = playedProgress.getAvailable();
		}
	}

	
	@AllowConcurrentEvents
	@Subscribe
	public void playAfterDownloadFinished(DownloadFinished e) {
		Song song = e.getSong();
		eventBus.post(new PlayIntent(song));
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPlayFinished(PlayStopped e) {
		songmaxSizeSetted = false;
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
			eventBus.post(new PlayIntent(nextSong));
		}
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void onPause(PlayPauseEvent e){
		if(isPaused==false){
			mayNextOnFinished = false;
			isPaused=true;
			player.pause();
		}else{
			isPaused=false;
			songmaxSizeSetted = true;
			player.resume();
		}
	}
	
	@AllowConcurrentEvents
	@Subscribe
	public void prev(PrevSong e) {
		Song prevSong = playList.getPrevSong(currentSong);
		if (prevSong != null) {
			LOGGER.debug("switching to prev");
			eventBus.post(new PlayIntent(prevSong));
		}
	}

	@AllowConcurrentEvents
	@Subscribe
	public void onStop(StopIntent e){
		mayNextOnFinished = false;
		player.stop();
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

	public boolean getPaused() {
		return isPaused;
	}
}
