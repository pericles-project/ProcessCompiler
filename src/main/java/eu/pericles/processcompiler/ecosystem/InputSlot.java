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
	
	//--------------- HASHCODE AND EQUALS ----------------//

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (optional ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputSlot other = (InputSlot) obj;
		if (optional != other.optional)
			return false;
		return true;
	}
	
	
}
