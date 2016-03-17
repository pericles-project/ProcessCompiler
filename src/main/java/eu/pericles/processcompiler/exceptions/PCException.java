package eu.pericles.processcompiler.exceptions;

/**
 * Process Compiler Exception, used as base class for all exceptions returned by
 * the Process Compiler.
 * 
 * @author noa
 *
 */
public class PCException extends Exception {

	private static final long serialVersionUID = 6072502108793503850L;

	public PCException(String message, Exception e) {
		super(message + e.getMessage());
	}

}
