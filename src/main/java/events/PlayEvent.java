package events;

public class PlayEvent {
	private String path;
	public PlayEvent(String path) {
		this.path = path;
	}
	public String getPath() {
		return path;
	}
}
