package player;

public interface Player {
	void play();
	void pause();
	void stop();
	void prepareFor(String path);
	State getState();
}
