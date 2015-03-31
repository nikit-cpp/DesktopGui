package player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.apache.log4j.Logger;

import com.google.common.eventbus.EventBus;

import events.PlayStopped;
import events.PlayStarted;
import events.PlayedProgress;
import javazoom.jl.player.Player;

public class CustomPlayer implements player.Player{
	private static final Logger LOGGER = Logger.getLogger(CustomPlayer.class);
	private Player player;
	private FileInputStream FIS;
	private BufferedInputStream BIS;
	private boolean canResume;
	private String path;
	private int total;
	private int stopped;
	private boolean valid;
	private EventBus eventBus;
	private State state;
	public static final int statusThreadSleep = 200;

	public CustomPlayer() {
		player = null;
		FIS = null;
		valid = false;
		BIS = null;
		path = null;
		total = 0;
		stopped = 0;
		canResume = false;
		state = State.STOPPED;
	}
	
	public CustomPlayer(EventBus eventBus) {
		player = null;
		FIS = null;
		valid = false;
		BIS = null;
		path = null;
		total = 0;
		stopped = 0;
		canResume = false;
		this.eventBus = eventBus;
		eventBus.register(this);
		state = State.STOPPED;
	}

	public boolean canResume() {
		return canResume;
	}

	public void prepareFor(String path) {
		this.path = path;
		stop();
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
			//state=State.STOPPED;
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
			FIS = new FileInputStream(path);
			total = FIS.available();
			if (pos > -1)
				FIS.skip(pos);
			BIS = new BufferedInputStream(FIS);
			player = new Player(BIS);
			LOGGER.debug("player in parent thread=" + player);
			Thread playThread = new Thread(new Runnable() {
				public synchronized void run() {
					try {
						LOGGER.debug("Playing " + path);
						post(new PlayStarted(path));
						state=State.PLAYING;
						LOGGER.debug("player in child thread=" + player + ", may be waiting if null");
						if (player==null)
							wait();
						player.play();
						state=State.STOPPED;
						post(new PlayStopped());
					} catch (Exception e) {
						LOGGER.error("Error playing mp3 file", e);
						valid = false;
					}
				}
			}, "playerThread");
			
			Thread statusThread = new Thread(new Runnable() {
				
				public synchronized void run() {
					try {
						LOGGER.debug("Starting statusThread");
						while(true){
							if(state == State.PLAYING){
								int available = FIS.available();
								LOGGER.debug("available: " + available);
								post(new PlayedProgress(available));
								Thread.sleep(statusThreadSleep);
							}
						}
					} catch (Exception e) {
						LOGGER.error("Error playing on statusThread", e);
					}
				}
			}, "statusThread");
			
			playThread.start();
			statusThread.start();
		} catch (Exception e) {
			LOGGER.error("Error playing mp3 file", e);
			valid = false;
		}
		return valid;
	}

	public void play() {
		play(-1);
	}
	
	private void post(Object o) {
		if(eventBus!=null)
			eventBus.post(o);
	}

	public State getState() {
		return state;
	}

}