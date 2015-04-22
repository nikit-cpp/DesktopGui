package player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;

import events.PlayStopped;
import events.PlayStarted;
import events.ProgressEvent;
import javazoom.jl.player.Player;

/**
 * Must be singletone
 * @author nik
 *
 */
public class CustomPlayer implements player.Player{
	private static final Logger LOGGER = Logger.getLogger(CustomPlayer.class);
	private volatile Player player;
	private volatile FileInputStream fis;
	private volatile BufferedInputStream bis;
	private volatile boolean canResume;
	private volatile int total;
	private volatile int stopped;
	private volatile boolean valid;
	private volatile EventBus eventBus;
	private volatile State state;
	private volatile Song playedSong;
	public static final int statusThreadSleep = 800;
	public static final int UNEXISTED_POSITION = -1; 

	public CustomPlayer(EventBus eventBus) {
		player = null;
		fis = null;
		valid = false;
		bis = null;
		total = 0;
		stopped = 0;
		canResume = false;
		if(eventBus!=null){
			this.eventBus = eventBus;
			eventBus.register(this);
		}
		state=State.STOPPED;
		
		
		
		
		ExecutorService es = Executors.newSingleThreadExecutor();
		es.submit(new Runnable() {
				public synchronized void run() {
					try {
						LOGGER.debug("Playing " + playedSong);
						post(new PlayStarted(playedSong));
						state=State.PLAYING;
						LOGGER.debug("player in child thread=" + player + "");

						player.play();
						state=State.STOPPED;
						post(new PlayStopped(playedSong));
					} catch (Exception e) {
						LOGGER.error("Error playing mp3 file", e);
						valid = false;
					}
				}
		}
		);
		
		
		
		
		startStatusThread();
	}

	public boolean canResume() {
		return canResume;
	}

	public void pause() {
		try {
			stopped = fis.available();
			player.close();
			fis = null;
			bis = null;
			player = null;
			if (valid)
				canResume = true;
			state=State.PAUSED;
			LOGGER.debug("Paused");
		} catch (Exception e) {
			LOGGER.error("Error on pause", e);
		}
	}
	
	public void stop() {
		try {
			if(player!=null)
				player.close();
			fis = null;
			bis = null;
			player = null;
			canResume = false;
			playedSong = null;
			LOGGER.debug("Stopped");
		} catch (Exception e) {
			LOGGER.error("Error on stop", e);
		}
	}

	public void resume() {
		if (!canResume)
			return;
		if (play(total - stopped))
			canResume = false;
	}

	synchronized private boolean play(int pos) {
		valid = true;
		canResume = false;
		try {
			fis = new FileInputStream(playedSong.getFile().getAbsolutePath());
			total = fis.available();
			if (pos > UNEXISTED_POSITION)
				fis.skip(pos);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);
			LOGGER.debug("player in parent thread=" + player);
			Thread playThread = new Thread(new Runnable() {
				public synchronized void run() {
					try {
						LOGGER.debug("Playing " + playedSong);
						post(new PlayStarted(playedSong));
						state=State.PLAYING;
						LOGGER.debug("player in child thread=" + player + "");

						player.play();
						state=State.STOPPED;
						post(new PlayStopped(playedSong));
					} catch (Exception e) {
						LOGGER.error("Error playing mp3 file", e);
						valid = false;
					}
				}
			}, "playerThread");
			playThread.start();
		} catch (Exception e) {
			LOGGER.error("Error playing mp3 file", e);
			valid = false;
		}
		return valid;
	}
	
	// assume wht CustomPlayer is singletone
	private void startStatusThread(){
		Thread statusThread = new Thread(new Runnable() {
			
			public synchronized void run() {
				try {
					LOGGER.debug("Starting statusThread");
					while(true){
						if(state == State.PLAYING){
							int available = fis.available();
							//LOGGER.debug("available: " + available);
							post(new ProgressEvent(available, playedSong));
							Thread.sleep(statusThreadSleep);
						}
					}
				} catch (Exception e) {
					LOGGER.error("Error playing on statusThread", e);
				}
			}
		}, "statusThread");
		statusThread.start();

	}

	public void play(Song song) {
		playedSong = song;
		play(UNEXISTED_POSITION);
	}
	
	private void post(Object o) {
		if(eventBus!=null)
			eventBus.post(o);
	}

	synchronized public State getState() {
		return state;
	}

}
