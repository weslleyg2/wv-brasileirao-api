package wv.webscraping.com.brasileiraoapi.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{
	
	private static final long seiralVersionUID = 1L;
	
	public BadRequestException() {
		super();
	}
	
	public BadRequestException(String message) {
		super(message);
	}
	
	public BadRequestException(Throwable cause) {
		super(cause);
	}
	public BadRequestException(String message, Throwable cause) {
		super(message,cause);
	}
}
