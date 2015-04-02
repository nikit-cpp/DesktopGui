package player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.apache.log4j.Logger;

import com.github.nikit.cpp.player.Song;
import com.google.common.eventbus.EventBus;

import events.PlayStopped;
import events.PlayStarted;
import events.PlayedProgress;
import javazoom.jl.player.Player;

/**
 * Must be singletone
 * @author nik
 *
 */
public class CustomPlayer implements player.Player{
	private static final Logger LOGGER = Logger.getLogger(CustomPlayer.class);
	private volatile Player player;
	private volatile FileInputStream FIS;
	private volatile BufferedInputStream BIS;
	private volatile boolean canResume;
	private volatile int total;
	private volatile int stopped;
	private volatile boolean valid;
	private volatile EventBus eventBus;
	private volatile State state;
	private volatile Song playedSong;
	public static final int statusThreadSleep = 200;
	public static final int UNEXISTED_POSITION = -1; 

	public CustomPlayer() {
		player = null;
		FIS = null;
		valid = false;
		BIS = null;
		total = 0;
		stopped = 0;
		canResume = false;
		state=State.STOPPED;
	}
	
	public CustomPlayer(EventBus eventBus) {
		player = null;
		FIS = null;
		valid = false;
		BIS = null;
		total = 0;
		stopped = 0;
		canResume = false;
		this.eventBus = eventBus;
		eventBus.register(this);
		state=State.STOPPED;
		startStatusThread();
	}

	public boolean canResume() {
		return canResume;
	}

	public void pause() {
		try {
			stopped = FIS.available();
			player.close();
			FIS = null;
			BIS = null;
			player = null;
			if (valid)
				canResume = true;
			state=State.STOPPED;
			LOGGER.debug("Paused");
		} catch (Exception e) {

		}
	}
	
	public void stop() {
		try {
			if(player!=null)
				player.close();
			FIS = null;
			BIS = null;
			player = null;
			canResume = false;
			playedSong = null;
			LOGGER.debug("Stopped");
		} catch (Exception e) {

		}
	}

	public void resume() {
		if (!canResume)
			return;
		if (play(total - stopped))
			canResume = false;
	}

	synchronized public boolean play(int pos) {
		valid = true;
		canResume = false;
		try {
			FIS = new FileInputStream(playedSong.getFile().getAbsolutePath());
			total = FIS.available();
			if (pos > UNEXISTED_POSITION)
				FIS.skip(pos);
			BIS = new BufferedInputStream(FIS);
			player = new Player(BIS);
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
						post(new PlayStopped());
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
							int available = FIS.available();
							//LOGGER.debug("available: " + available);
							post(new PlayedProgress(available, playedSong));
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
