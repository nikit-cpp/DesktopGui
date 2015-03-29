package service;

public class DownloadServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public DownloadServiceException(String message) {
        super(message);
    }
	
	public DownloadServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
