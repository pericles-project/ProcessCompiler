package eu.pericles.processcompiler.ecosystem;

public class InputSlot extends Slot {
	
	private boolean optional;

	public boolean isOptional() {
		return optional;
	}
	
	//--------------- GETTERS AND SETTERS ----------------//
	
	public void setOptional(boolean optional) {
		this.optional = optional;
	}
}
