package player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import org.apache.log4j.Logger;
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

	public CustomPlayer() {
		player = null;
		FIS = null;
		valid = false;
		BIS = null;
		path = null;
		total = 0;
		stopped = 0;
		canResume = false;
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

	public boolean play(int pos) {
		valid = true;
		canResume = false;
		try {
			FIS = new FileInputStream(path);
			total = FIS.available();
			if (pos > -1)
				FIS.skip(pos);
			BIS = new BufferedInputStream(FIS);
			player = new Player(BIS);
			LOGGER.debug("Playing " + path);
			player.play();
		} catch (Exception e) {
			LOGGER.error("Error playing mp3 file", e);
			valid = false;
		}
		return valid;
	}

	public void play() {
		play(-1);
	}
}