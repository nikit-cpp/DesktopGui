package service;

public class PlayerServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public PlayerServiceException(String message) {
        super(message);
    }
	
	public PlayerServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
