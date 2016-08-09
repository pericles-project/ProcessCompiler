package eu.pericles.processcompiler.exceptions;

/**
 * Process Compiler Exception, used as base class for all exceptions returned by
 * the Process Compiler.
 */
public class BaseException extends Exception {

	private static final long serialVersionUID = 6072502108793503850L;

	public BaseException(String message) {
		super(message);
	}
	
	public BaseException(String message, Exception e) {
		super(message);
	}

}
