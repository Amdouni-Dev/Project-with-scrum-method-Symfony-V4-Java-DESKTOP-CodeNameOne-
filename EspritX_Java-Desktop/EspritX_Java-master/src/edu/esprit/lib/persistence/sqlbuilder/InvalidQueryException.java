package edu.esprit.lib.persistence.sqlbuilder;

public class InvalidQueryException extends RuntimeException {

	public InvalidQueryException() {}

	public InvalidQueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidQueryException(String message) {
		super(message);
	}

	public InvalidQueryException(Throwable cause) {
		super(cause);
	}
}
