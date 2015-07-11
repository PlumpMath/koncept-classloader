package koncept.classloader.exception;

public class KonceptClassException extends RuntimeException {

	public KonceptClassException(String message) {
		super(message);
	}
	
	public KonceptClassException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public KonceptClassException(Throwable cause) {
		super(cause);
	}
	
}
