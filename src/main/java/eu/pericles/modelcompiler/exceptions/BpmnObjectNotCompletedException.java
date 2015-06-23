package eu.pericles.modelcompiler.exceptions;

public class BpmnObjectNotCompletedException extends Exception{

	private static final long serialVersionUID = 8271608369200244160L;

	public BpmnObjectNotCompletedException(String message) {
		super(message);
	}
}
