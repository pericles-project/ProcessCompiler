package eu.pericles.processcompiler.exceptions;

public class ValidationException extends PCException {

	private static final long serialVersionUID = 5035038260506935669L;

	public ValidationException(String message, Exception e) {
		super(message, e);
	}
}
